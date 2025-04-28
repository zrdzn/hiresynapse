package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zrdzn.hiresynapse.hiresynapsebackend.ai.AiClient;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.InterviewCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.event.InterviewCreateEvent;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Interview;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.InterviewRepository;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.ai.AiPrompts.INTERVIEW_QUESTIONS_PROMPT;

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
    private final MongoTemplate mongoTemplate;

    public InterviewService(
        InterviewRepository interviewRepository,
        UserService userService,
        CandidateService candidateService,
        TaskService taskService,
        AiClient aiClient,
        ObjectMapper objectMapper,
        MongoTemplate mongoTemplate
    ) {
        this.interviewRepository = interviewRepository;
        this.userService = userService;
        this.candidateService = candidateService;
        this.taskService = taskService;
        this.aiClient = aiClient;
        this.objectMapper = objectMapper;
        this.mongoTemplate = mongoTemplate;
    }

    public Interview initiateInterviewCreation(
        String recruiterId,
        @Valid InterviewCreateDto interviewCreateDto
    ) {
        User recruiter = userService.getUser(recruiterId).orElseThrow();
        Candidate candidate = candidateService.getCandidate(interviewCreateDto.candidateId()).orElseThrow();

        if (interviewCreateDto.interviewStatus() == InterviewStatus.CANCELLED) {
            throw new IllegalArgumentException("New interview cannot be created with cancelled status");
        }

        if (interviewCreateDto.interviewStatus() == InterviewStatus.COMPLETED) {
            throw new IllegalArgumentException("New interview cannot be created with completed status");
        }

        Interview interview = new Interview(
            null,
            recruiter,
            candidate,
            null,
            null,
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

        taskService.sendEvent(
            createdInterview.getId(),
            new InterviewCreateEvent(
                createdInterview.getId(),
                candidate.getId(),
                interviewCreateDto.interviewType(),
                interviewCreateDto.enableQuestions(),
                interviewCreateDto.questionsAmount()
            )
        );

        logger.info("Started interview creation task for interview: {}", createdInterview.getId());

        return createdInterview;
    }

    public CompletableFuture<Interview> processInterview(InterviewCreateEvent interviewCreateEvent) {
        return CompletableFuture
            .supplyAsync(() -> {
                logger.info("Processing interview: {}", interviewCreateEvent.interviewReferenceId());

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
                    logger.info("Questions are disabled, skipping...");
                    return;
                }

                if (response.startsWith("```json")) {
                    response = response.substring(7, response.length() - 3).trim();
                }

                try {
                    List<String> questions = objectMapper.readValue(response, new TypeReference<>() {});

                    logger.info("Questions were successfully generated: {}", questions);

                    updateInterview(
                        interviewCreateEvent.interviewReferenceId(),
                        questions
                    );
                } catch (JsonProcessingException exception) {
                    throw new RuntimeException(exception);
                }
            })
            .thenRun(() -> updateInterviewTaskStatus(interviewCreateEvent.interviewReferenceId(), TaskStatus.COMPLETED))
            .thenApply(unused -> getInterview(interviewCreateEvent.interviewReferenceId()).orElseThrow())
            .exceptionally(exception -> {
                logger.error("Error processing interview", exception);

                updateInterviewTaskStatus(interviewCreateEvent.interviewReferenceId(), TaskStatus.FAILED);

                throw new CompletionException(exception);
            });
    }

    public void updateInterview(
        String interviewId,
        List<String> questions
    ) {
        Query query = new Query(Criteria.where("_id").is(interviewId));
        Update update = new Update()
            .set("questions", questions);

        mongoTemplate.updateFirst(query, update, Interview.class);

        logger.info("Updated interview: {}", interviewId);
    }

    public void updateInterviewTaskStatus(String interviewId, TaskStatus status) {
        Query query = new Query(Criteria.where("_id").is(interviewId));
        Update update = new Update().set("taskStatus", status);
        mongoTemplate.updateFirst(query, update, Interview.class);

        logger.info("Updated interview status: {} to {}", interviewId, status);
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

    public Optional<Interview> getInterview(String interviewId) {
        return interviewRepository.findById(interviewId);
    }

}
