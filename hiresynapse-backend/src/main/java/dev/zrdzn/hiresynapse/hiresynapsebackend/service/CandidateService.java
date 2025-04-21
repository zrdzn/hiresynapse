package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zrdzn.hiresynapse.hiresynapsebackend.ai.AiClient;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.AnalysedResumeDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.CandidateCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.event.CandidateCreateEvent;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.CandidateStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.CandidateRepository;
import jakarta.validation.Valid;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.ai.AiPrompts.CANDIDATE_RESUME_ANALYZE_PROMPT;

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
    private final MongoTemplate mongoTemplate;

    public CandidateService(
        CandidateRepository candidateRepository,
        JobService jobService,
        TaskService taskService,
        AiClient aiClient,
        DocumentExtractorService documentExtractorService,
        ObjectMapper objectMapper,
        MongoTemplate mongoTemplate
    ) {
        this.candidateRepository = candidateRepository;
        this.jobService = jobService;
        this.taskService = taskService;
        this.aiClient = aiClient;
        this.documentExtractorService = documentExtractorService;
        this.objectMapper = objectMapper;
        this.mongoTemplate = mongoTemplate;
    }

    public Candidate initiateCandidateCreation(
        @Valid CandidateCreateDto candidateCreateDto,
        String resumeFileName,
        MultipartFile resumeFile
    ) {
        Job job = jobService.getJob(candidateCreateDto.jobId()).orElseThrow();

        Candidate candidate = new Candidate(
            null,
            job,
            candidateCreateDto.email(),
            CandidateStatus.PENDING,
            TaskStatus.PENDING,
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

        Path path;
        try {
            File tempFile = File.createTempFile(
                createdCandidate.getId(),
                resumeFileName.substring(resumeFileName.lastIndexOf('.'))
            );
            tempFile.deleteOnExit();

            path = tempFile.toPath();

            Files.copy(resumeFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            logger.info("Created temp file: {}", path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        taskService.sendEvent(
            createdCandidate.getId(),
            new CandidateCreateEvent(createdCandidate.getId(), job.getId(), resumeFileName, path.toString(), resumeFile.getContentType())
        );

        logger.info("Started candidate creation task for candidate: {}", createdCandidate.getId());

        return createdCandidate;
    }

    public CompletableFuture<Candidate> processCandidate(CandidateCreateEvent candidateCreateEvent) {
        return CompletableFuture
            .supplyAsync(() -> {
                logger.info("Processing candidate: {}", candidateCreateEvent.candidateReferenceId());

                logger.info("Extracting data from resume sent by candidate: {}", candidateCreateEvent.candidateReferenceId());
                String resumeContent;
                Path path = Path.of(candidateCreateEvent.resumeFilePath());
                try (InputStream resumeFileStream = Files.newInputStream(path)) {
                    resumeContent = documentExtractorService.extractText(candidateCreateEvent.resumeFileName(), resumeFileStream);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to extract text from resume file", e);
                } finally {
                    try {
                        Files.deleteIfExists(path);
                        logger.info("Deleted temp file: {}", path);
                    } catch (IOException e) {
                        logger.warn("Failed to delete temp file: {}", path, e);
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

                try {
                    AnalysedResumeDto analysedResume = objectMapper.readValue(response, AnalysedResumeDto.class);

                    logger.info("Resume successfully analysed, updating candidate...");

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
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            })
            .thenRun(() -> updateCandidateStatus(candidateCreateEvent.candidateReferenceId(), TaskStatus.COMPLETED))
            .thenApply(unused -> getCandidate(candidateCreateEvent.candidateReferenceId()).orElseThrow())
            .exceptionally(e -> {
                logger.error("Error processing candidate", e);

                updateCandidateStatus(candidateCreateEvent.candidateReferenceId(), TaskStatus.FAILED);

                throw new CompletionException(e);
            });
    }

    public void updateCandidate(
        String candidateId,
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
        Query query = new Query(Criteria.where("_id").is(candidateId));
        Update update = new Update()
            .set("firstName", firstName)
            .set("lastName", lastName)
            .set("phone", phone)
            .set("executiveSummary", executiveSummary)
            .set("analysedSummary", analysedSummary)
            .set("careerTrajectory", careerTrajectory)
            .set("yearsOfRelatedExperience", yearsOfRelatedExperience)
            .set("yearsOfExperience", yearsOfExperience)
            .set("relatedExperience", relatedExperience)
            .set("experience", experience)
            .set("education", education)
            .set("skills", skills)
            .set("projects", projects)
            .set("languages", languages)
            .set("certificates", certificates)
            .set("references", references)
            .set("keyAchievements", keyAchievements)
            .set("keySoftSkills", keySoftSkills);

        mongoTemplate.updateFirst(query, update, Candidate.class);

        logger.info("Updated candidate: {}", candidateId);
    }

    public void updateCandidateStatus(String candidateId, TaskStatus status) {
        Query query = new Query(Criteria.where("_id").is(candidateId));
        Update update = new Update().set("taskStatus", status);
        mongoTemplate.updateFirst(query, update, Candidate.class);

        logger.info("Updated candidate status: {} to {}", candidateId, status);
    }

    public List<Candidate> getCandidates(Pageable pageable) {
        return candidateRepository.findAllByTaskStatus(TaskStatus.COMPLETED, pageable).getContent();
    }

    public Optional<Candidate> getCandidate(String candidateId) {
        return candidateRepository.findById(candidateId);
    }

    private <K, V> Map<K, V> emptyMapIfNull(Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    private <T> List<T> emptyListIfNull(List<T> list) {
        return list == null ? Collections.emptyList() : list;
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
