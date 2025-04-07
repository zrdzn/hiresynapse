package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.CandidateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class CandidateService {

    private final Logger logger = LoggerFactory.getLogger(CandidateService.class);

    private final CandidateRepository candidateRepository;
    private final TaskService taskService;

    public CandidateService(CandidateRepository candidateRepository, TaskService taskService) {
        this.candidateRepository = candidateRepository;
        this.taskService = taskService;
    }

    public Candidate initiateCandidateCreation(Candidate candidate) {
        Candidate createdCandidate = candidateRepository.save(candidate);

        taskService.createTaskAndDispatch(
            TaskStatus.PENDING,
            createdCandidate,
            null,
            createdCandidate.getEmail()
        );

        logger.info("Started candidate creation task for candidate: {}", createdCandidate.getId());

        return createdCandidate;
    }

    public CompletableFuture<Candidate> processCandidate(Candidate candidate) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Processing candidate: {}", candidate.getId());

            return candidate;
        }).exceptionally(e -> {
            logger.error("Error processing candidate", e);

            throw new CompletionException(e);
        });
    }

    public List<Candidate> getCandidates(String requesterId, Pageable pageable) {
        return candidateRepository.findAll(pageable).getContent();
    }

}
