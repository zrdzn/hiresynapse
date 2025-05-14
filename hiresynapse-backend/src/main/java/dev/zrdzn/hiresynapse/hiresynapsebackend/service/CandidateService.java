package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zrdzn.hiresynapse.hiresynapsebackend.ai.AiClient;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.AnalysedResumeDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.CandidateCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.JobTitleCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.MonthlyDataDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.UtmSourceCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.error.ApiError;
import dev.zrdzn.hiresynapse.hiresynapsebackend.event.CandidateCreateEvent;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.CandidateStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogAction;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogEntityType;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.CandidateRepository;
import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.statistic.StatisticHelper;
import jakarta.validation.Valid;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.ai.AiPrompts.CANDIDATE_RESUME_ANALYZE_PROMPT;
import static dev.zrdzn.hiresynapse.hiresynapsebackend.shared.CollectionHelper.emptyListIfNull;
import static dev.zrdzn.hiresynapse.hiresynapsebackend.shared.CollectionHelper.emptyMapIfNull;

@Service
@Validated
public class CandidateService {

    private final Logger logger = LoggerFactory.getLogger(CandidateService.class);

    private final CandidateRepository candidateRepository;
    private final JobService jobService;
    private final TaskService taskService;
    private final AiClient aiClient;
    private final DocumentExtractorService documentExtractorService;
    private final ObjectMapper objectMapper;
    private final LogService logService;

    public CandidateService(
        CandidateRepository candidateRepository,
        JobService jobService,
        TaskService taskService,
        AiClient aiClient,
        DocumentExtractorService documentExtractorService,
        ObjectMapper objectMapper,
        LogService logService
    ) {
        this.candidateRepository = candidateRepository;
        this.jobService = jobService;
        this.taskService = taskService;
        this.aiClient = aiClient;
        this.documentExtractorService = documentExtractorService;
        this.objectMapper = objectMapper;
        this.logService = logService;
    }

    public Candidate initiateCandidateCreation(
        @Valid CandidateCreateDto candidateCreateDto,
        String resumeFileName,
        MultipartFile resumeFile
    ) {
        Job job = jobService.getJob(candidateCreateDto.jobId()).orElseThrow();

        Candidate candidate = new Candidate(
            null,
            null,
            null,
            job,
            candidateCreateDto.email(),
            CandidateStatus.PENDING,
            TaskStatus.PENDING,
            candidateCreateDto.utmSource(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        Candidate createdCandidate = candidateRepository.save(candidate);

        logService.createLog(
            null,
            "New candidate has applied for job",
            LogAction.CREATE,
            LogEntityType.CANDIDATE,
            createdCandidate.getId()
        );

        Path path;
        try {
            File tempFile = File.createTempFile(
                createdCandidate.getId().toString() + "-" + createdCandidate.getEmail(),
                resumeFileName.substring(resumeFileName.lastIndexOf('.'))
            );
            tempFile.deleteOnExit();

            path = tempFile.toPath();

            Files.copy(resumeFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            logger.debug("Created temp file: {}", path);
        } catch (IOException exception) {
            logger.error("Failed to create temp file", exception);
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create temp file");
        }

        taskService.sendEvent(
            createdCandidate.getId(),
            new CandidateCreateEvent(createdCandidate.getId(), job.getId(), resumeFileName, path.toString(), resumeFile.getContentType())
        );

        logger.debug("Started candidate creation task for candidate: {}", createdCandidate.getId());

        return createdCandidate;
    }

    public CompletableFuture<Candidate> processCandidate(CandidateCreateEvent candidateCreateEvent) {
        return CompletableFuture
            .supplyAsync(() -> {
                logger.debug("Processing candidate: {}", candidateCreateEvent.candidateReferenceId());

                logger.debug("Extracting data from resume sent by candidate: {}", candidateCreateEvent.candidateReferenceId());
                String resumeContent;
                Path path = Path.of(candidateCreateEvent.resumeFilePath());
                try (InputStream resumeFileStream = Files.newInputStream(path)) {
                    resumeContent = documentExtractorService.extractText(candidateCreateEvent.resumeFileName(), resumeFileStream);
                } catch (IOException exception) {
                    throw new RuntimeException("Failed to extract text from resume file", exception);
                } finally {
                    try {
                        Files.deleteIfExists(path);
                        logger.debug("Deleted temp file: {}", path);
                    } catch (IOException exception) {
                        logger.warn("Failed to delete temp file: {}", path, exception);
                    }
                }

                Job job = jobService.getJob(candidateCreateEvent.jobId()).orElseThrow();

                Map<String, String> replacements = Map.of(
                    "TITLE", job.getTitle(),
                    "CONTENT", resumeContent,
                    "YEAR", String.valueOf(Year.now().getValue())
                );
                StringSubstitutor substitutor = new StringSubstitutor(replacements , "{", "}");

                return substitutor.replace(CANDIDATE_RESUME_ANALYZE_PROMPT);
            })
            .thenCompose(aiClient::sendPrompt)
            .thenAccept(response -> {
                if (response.startsWith("```json")) {
                    response = response.substring(7, response.length() - 3).trim();
                }

                logger.debug("Received response from AI: {}", response);

                try {
                    AnalysedResumeDto analysedResume = objectMapper.readValue(response, AnalysedResumeDto.class);

                    logger.debug("Resume successfully analysed, updating candidate...");

                    updateCandidate(
                        candidateCreateEvent.candidateReferenceId(),
                        analysedResume.firstName(),
                        analysedResume.lastName(),
                        analysedResume.phone(),
                        analysedResume.executiveSummary(),
                        analysedResume.analysedSummary(),
                        analysedResume.careerTrajectory(),
                        calculateYearsOfExperience(analysedResume.relatedExperience()),
                        calculateYearsOfExperience(analysedResume.experience()),
                        emptyMapIfNull(analysedResume.relatedExperience()),
                        emptyMapIfNull(analysedResume.experience()),
                        emptyMapIfNull(analysedResume.education()),
                        emptyMapIfNull(analysedResume.skills()),
                        emptyMapIfNull(analysedResume.projects()),
                        emptyListIfNull(analysedResume.languages()),
                        emptyListIfNull(analysedResume.certificates()),
                        emptyListIfNull(analysedResume.references()),
                        emptyListIfNull(analysedResume.keyAchievements()),
                        emptyListIfNull(analysedResume.keySoftSkills())
                    );
                } catch (JsonProcessingException exception) {
                    throw new RuntimeException(exception);
                }
            })
            .thenRun(() -> updateCandidateStatus(candidateCreateEvent.candidateReferenceId(), TaskStatus.COMPLETED))
            .thenApply(unused -> getCandidate(candidateCreateEvent.candidateReferenceId()).orElseThrow())
            .exceptionally(exception -> {
                logger.error("Error processing candidate", exception);

                updateCandidateStatus(candidateCreateEvent.candidateReferenceId(), TaskStatus.FAILED);

                throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process candidate");
            });
    }

    public void updateCandidate(
        long candidateId,
        String firstName,
        String lastName,
        String phone,
        String executiveSummary,
        String analysedSummary,
        String careerTrajectory,
        Integer yearsOfRelatedExperience,
        Integer yearsOfExperience,
        Map<String, String> relatedExperience,
        Map<String, String> experience,
        Map<String, String> education,
        Map<String, String> skills,
        Map<String, String> projects,
        List<String> languages,
        List<String> certificates,
        List<String> references,
        List<String> keyAchievements,
        List<String> keySoftSkills
    ) {
        Candidate candidate = candidateRepository.findById(candidateId)
            .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND, "Candidate not found"));

        candidate.setFirstName(firstName);
        candidate.setLastName(lastName);
        candidate.setPhone(phone);
        candidate.setExecutiveSummary(executiveSummary);
        candidate.setAnalysedSummary(analysedSummary);
        candidate.setCareerTrajectory(careerTrajectory);
        candidate.setYearsOfRelatedExperience(yearsOfRelatedExperience);
        candidate.setYearsOfExperience(yearsOfExperience);
        candidate.setRelatedExperience(relatedExperience);
        candidate.setExperience(experience);
        candidate.setEducation(education);
        candidate.setSkills(skills);
        candidate.setProjects(projects);
        candidate.setLanguages(languages);
        candidate.setCertificates(certificates);
        candidate.setEmployerReferences(references);
        candidate.setKeyAchievements(keyAchievements);
        candidate.setKeySoftSkills(keySoftSkills);

        candidateRepository.save(candidate);

        logService.createLog(
            null,
            "Candidate has been updated",
            LogAction.UPDATE,
            LogEntityType.CANDIDATE,
            candidate.getId()
        );

        logger.debug("Updated candidate: {}", candidateId);
    }

    public void updateCandidateStatus(long candidateId, TaskStatus status) {
        Candidate candidate = candidateRepository.findById(candidateId)
            .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND, "Candidate not found"));

        candidate.setTaskStatus(status);

        candidateRepository.save(candidate);

        logService.createLog(
            null,
            "Candidate task status updated to " + status,
            LogAction.UPDATE,
            LogEntityType.CANDIDATE,
            candidate.getId()
        );

        logger.debug("Updated candidate status: {} to {}", candidateId, status);
    }

    public List<Candidate> getCandidates(Pageable pageable) {
        return candidateRepository.findAllByTaskStatus(TaskStatus.COMPLETED, pageable).getContent();
    }

    public List<Candidate> getPendingCandidates(Pageable pageable) {
        return candidateRepository.findAllByTaskStatusAndStatus(TaskStatus.COMPLETED, CandidateStatus.PENDING, pageable).getContent();
    }

    public Optional<Candidate> getCandidate(long candidateId) {
        return candidateRepository.findById(candidateId);
    }

    public int getCandidateCount() {
        return (int) candidateRepository.count();
    }

    public List<JobTitleCountDto> getJobTitleCount() {
        List<JobTitleCountDto> jobTitleCounts = candidateRepository.findJobTitleCounts();

        List<JobTitleCountDto> filteredResults = new LinkedList<>();
        long totalJobs = 0;

        for (JobTitleCountDto jobTitleCount : jobTitleCounts) {
            if (jobTitleCount.count() > 0 && jobTitleCount.title() != null) {
                filteredResults.add(jobTitleCount);
                totalJobs += jobTitleCount.count();
            }
        }

        long finalCount = totalJobs;

        return filteredResults.stream()
            .map(jobTitleCount -> new JobTitleCountDto(
                jobTitleCount.title(),
                jobTitleCount.count(),
                (double) jobTitleCount.count() / finalCount * 100
            ))
            .toList();
    }

    public List<UtmSourceCountDto> getUtmSourceCount() {
        List<UtmSourceCountDto> utmSourceCounts = candidateRepository.findUtmSourceCounts();

        List<UtmSourceCountDto> filteredResults = new LinkedList<>();
        long totalSources = 0;

        // include only non-empty interview types
        for (UtmSourceCountDto utmSourceCount : utmSourceCounts) {
            if (utmSourceCount.count() > 0 && utmSourceCount.source() != null) {
                filteredResults.add(utmSourceCount);
                totalSources += utmSourceCount.count();
            }
        }

        long finalTotalSources = totalSources;

        return filteredResults.stream()
            .map(utmSource -> new UtmSourceCountDto(
                utmSource.source(),
                utmSource.count(),
                (double) utmSource.count() / finalTotalSources * 100
            ))
            .toList();
    }

    public MonthlyDataDto getCandidatesFromLastSixMonths() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        Instant startDate = sixMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Candidate> candidates = candidateRepository.findCandidatesCreatedAfter(startDate);

        Map<String, Integer> monthlyData = StatisticHelper.countByMonth(candidates);
        double growthRate = StatisticHelper.calculateGrowthRate(monthlyData);

        return new MonthlyDataDto(
            candidates.size(),
            growthRate,
            monthlyData
        );
    }

    public MonthlyDataDto getCandidatesFromLastSixMonths(CandidateStatus status) {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        Instant startDate = sixMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Candidate> candidates = candidateRepository.findCandidatesCreatedAfter(startDate, status);

        Map<String, Integer> monthlyData = StatisticHelper.countByMonth(candidates);
        double growthRate = StatisticHelper.calculateGrowthRate(monthlyData);

        return new MonthlyDataDto(
            candidates.size(),
            growthRate,
            monthlyData
        );
    }

    private int calculateYearsOfExperience(Map<String, String> experience) {
        if (experience == null) {
            return 0;
        }

        if (experience.isEmpty()) {
            return 0;
        }

        Set<Integer> yearsWorked = new HashSet<>();

        experience.values()
            .forEach(period -> {
                String[] periodRange = period.split("-");
                if (periodRange.length == 2) {
                    int startYear = Integer.parseInt(periodRange[0]);
                    int endYear = Integer.parseInt(periodRange[1]);

                    for (int year = startYear; year <= endYear; year++) {
                        yearsWorked.add(year);
                    }
                } else {
                    logger.warn("Invalid period format: {}", period);
                }
            });

        return yearsWorked.size();
    }

}
