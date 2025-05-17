package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zrdzn.hiresynapse.hiresynapsebackend.ai.AiClient;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.interview.InterviewCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.interview.InterviewStatusCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.interview.InterviewTypeCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.MonthlyDataDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.error.ApiError;
import dev.zrdzn.hiresynapse.hiresynapsebackend.event.InterviewCreateEvent;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.Interview;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.InterviewStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogAction;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogEntityType;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.InterviewRepository;
import jakarta.validation.Valid;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.ai.AiPrompts.INTERVIEW_QUESTIONS_PROMPT;
import static dev.zrdzn.hiresynapse.hiresynapsebackend.shared.statistic.StatisticHelper.getMonthlyData;

@Service
@Validated
public class InterviewService {

    private final Logger logger = LoggerFactory.getLogger(InterviewService.class);

    private final InterviewRepository interviewRepository;
    private final UserService userService;
    private final CandidateService candidateService;
    private final TaskService taskService;
    private final AiClient aiClient;
    private final ObjectMapper objectMapper;
    private final LogService logService;

    public InterviewService(
        InterviewRepository interviewRepository,
        UserService userService,
        CandidateService candidateService,
        TaskService taskService,
        AiClient aiClient,
        ObjectMapper objectMapper,
        LogService logService
    ) {
        this.interviewRepository = interviewRepository;
        this.userService = userService;
        this.candidateService = candidateService;
        this.taskService = taskService;
        this.aiClient = aiClient;
        this.objectMapper = objectMapper;
        this.logService = logService;
    }

    public Interview initiateInterviewCreation(
        long recruiterId,
        @Valid InterviewCreateDto interviewCreateDto
    ) {
        User recruiter = userService.getUser(recruiterId).orElseThrow();
        Candidate candidate = candidateService.getCandidate(interviewCreateDto.candidateId()).orElseThrow();

        if (interviewCreateDto.interviewStatus() == InterviewStatus.CANCELLED) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "New interview cannot be created with cancelled status");
        }

        if (interviewCreateDto.interviewStatus() == InterviewStatus.COMPLETED) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "New interview cannot be created with completed status");
        }

        Interview interview = new Interview(
            null,
            null,
            null,
            recruiter,
            candidate,
            interviewCreateDto.interviewStatus(),
            TaskStatus.PENDING,
            interviewCreateDto.interviewAt(),
            null,
            null,
            null,
            interviewCreateDto.interviewType(),
            interviewCreateDto.notes(),
            null,
            interviewCreateDto.enableQuestions(),
            interviewCreateDto.questionsAmount(),
            null
        );

        Interview createdInterview = interviewRepository.save(interview);

        logService.createLog(
            recruiterId,
            "Interview has been scheduled",
            LogAction.CREATE,
            LogEntityType.INTERVIEW,
            createdInterview.getId()
        );

        taskService.sendEvent(
            createdInterview.getId(),
            new InterviewCreateEvent(
                recruiterId,
                createdInterview.getId(),
                candidate.getId(),
                interviewCreateDto.interviewType(),
                interviewCreateDto.enableQuestions(),
                interviewCreateDto.questionsAmount()
            )
        );

        logger.debug("Started interview creation task for interview: {}", createdInterview.getId());

        return createdInterview;
    }

    public CompletableFuture<Interview> processInterview(InterviewCreateEvent interviewCreateEvent) {
        return CompletableFuture
            .supplyAsync(() -> {
                logger.debug("Processing interview: {}", interviewCreateEvent.interviewReferenceId());

                Candidate candidate = candidateService.getCandidate(interviewCreateEvent.candidateId()).orElseThrow();

                Map<String, String> replacements = Map.of(
                    "AMOUNT", String.valueOf(interviewCreateEvent.questionsAmount()),
                    "INTERVIEW_TYPE", String.valueOf(interviewCreateEvent.interviewType()),
                    "TITLE", candidate.getJob().getTitle(),
                    "EXPERIENCE", String.join(";", candidate.getExperience().keySet()),
                    "SKILLS", String.join(";", candidate.getSkills().keySet()),
                    "PROJECTS", String.join(";", candidate.getProjects().keySet()),
                    "SUMMARY", candidate.getAnalysedSummary()
                );
                StringSubstitutor substitutor = new StringSubstitutor(replacements , "{", "}");

                return substitutor.replace(INTERVIEW_QUESTIONS_PROMPT);
            })
            .thenCompose(aiClient::sendPrompt)
            .thenAccept(response -> {
                if (!interviewCreateEvent.enableQuestions()) {
                    logger.debug("Questions are disabled, skipping...");
                    return;
                }

                if (response.startsWith("```json")) {
                    response = response.substring(7, response.length() - 3).trim();
                }

                try {
                    List<String> questions = objectMapper.readValue(response, new TypeReference<>() {});

                    logger.debug("Questions were successfully generated: {}", questions);

                    updateInterview(
                        interviewCreateEvent.recruiterId(),
                        interviewCreateEvent.interviewReferenceId(),
                        questions
                    );
                } catch (JsonProcessingException exception) {
                    throw new RuntimeException(exception);
                }
            })
            .thenRun(() -> updateInterviewTaskStatus(
                interviewCreateEvent.recruiterId(),
                interviewCreateEvent.interviewReferenceId(),
                TaskStatus.COMPLETED
                )
            )
            .thenApply(unused -> getInterview(interviewCreateEvent.interviewReferenceId()).orElseThrow())
            .exceptionally(exception -> {
                logger.error("Error processing interview", exception);

                updateInterviewTaskStatus(
                    interviewCreateEvent.recruiterId(),
                    interviewCreateEvent.interviewReferenceId(),
                    TaskStatus.FAILED
                );

                throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process interview");
            });
    }

    public void updateInterview(
        long requesterId,
        long interviewId,
        List<String> questions
    ) {
        Interview interview = interviewRepository.findById(interviewId)
            .orElseThrow(() -> new IllegalArgumentException("Interview not found"));

        interview.setQuestions(questions);

        interviewRepository.save(interview);

        logService.createLog(
            requesterId,
            "Interview questions have been updated",
            LogAction.UPDATE,
            LogEntityType.INTERVIEW,
            interview.getId()
        );

        logger.debug("Updated interview: {}", interviewId);
    }

    public void updateInterviewTaskStatus(
        long requesterId,
        long interviewId,
        TaskStatus status
    ) {
        Interview interview = interviewRepository.findById(interviewId)
            .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND, "Interview not found"));

        interview.setTaskStatus(status);

        interviewRepository.save(interview);

        logService.createLog(
            requesterId,
            "Interview task status updated to " + status,
            LogAction.UPDATE,
            LogEntityType.INTERVIEW,
            interview.getId()
        );

        logger.debug("Updated interview status: {} to {}", interviewId, status);
    }

    public List<Interview> getInterviews(Pageable pageable) {
        return interviewRepository.findAllByTaskStatus(TaskStatus.COMPLETED, pageable).getContent();
    }

    public List<Interview> getConfirmedInterviews(Pageable pageable) {
        return interviewRepository.findByStatusAndTaskStatusOrderByInterviewAtAsc(
            InterviewStatus.CONFIRMED,
            TaskStatus.COMPLETED,
            pageable
        ).getContent();
    }

    public List<Interview> getUnconfirmedInterviews(Pageable pageable) {
        return interviewRepository.findByStatusAndTaskStatusOrderByInterviewAtAsc(
            InterviewStatus.SCHEDULED,
            TaskStatus.COMPLETED,
            pageable
        ).getContent();
    }

    public Optional<Interview> getInterview(long interviewId) {
        return interviewRepository.findById(interviewId);
    }

    public int getInterviewCount() {
        return (int) interviewRepository.count();
    }

    public List<InterviewStatusCountDto> getInterviewStatusCount() {
        List<InterviewStatusCountDto> interviewStatusCounts = interviewRepository.findInterviewStatusCounts();

        long totalInterviews = interviewStatusCounts.stream()
            .mapToLong(InterviewStatusCountDto::count)
            .sum();

        return interviewStatusCounts.stream()
            .map(interview -> new InterviewStatusCountDto(
                interview.status(),
                interview.count(),
                (double) interview.count() / totalInterviews * 100
            ))
            .toList();
    }

    public List<InterviewTypeCountDto> getInterviewTypeCount() {
        List<InterviewTypeCountDto> interviewTypeCounts = interviewRepository.findInterviewTypeCounts();

        long totalInterviews = interviewTypeCounts.stream()
            .mapToLong(InterviewTypeCountDto::count)
            .sum();

        return interviewTypeCounts.stream()
            .map(interview -> new InterviewTypeCountDto(
                interview.interviewType(),
                interview.count(),
                (double) interview.count() / totalInterviews * 100
            ))
            .toList();
    }

    public MonthlyDataDto getInterviewsFromLastSixMonths() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        Instant startDate = sixMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Interview> interviews = interviewRepository.findInterviewsCreatedAfter(startDate);

        return getMonthlyData(interviews);
    }

    public MonthlyDataDto getInterviewsFromLastSixMonths(InterviewStatus status) {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        Instant startDate = sixMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Interview> interviews = interviewRepository.findInterviewsCreatedAfter(startDate, status);

        return getMonthlyData(interviews);
    }

    public void deleteInterview(long requesterId, long interviewId) {
        interviewRepository.deleteById(interviewId);

        logService.createLog(
            requesterId,
            "Interview has been deleted",
            LogAction.DELETE,
            LogEntityType.INTERVIEW,
            interviewId
        );

        logger.debug("Interview with id {} deleted", interviewId);
    }

}
