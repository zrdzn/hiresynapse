package dev.zrdzn.hiresynapse.hiresynapsebackend.listener;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.CandidateService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.JobService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.CANDIDATE_TOPIC;
import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.JOB_TOPIC;

@Service
public class TaskListener {

    private final Logger logger = LoggerFactory.getLogger(TaskListener.class);

    private final TaskService taskService;
    private final CandidateService candidateService;
    private final JobService jobService;

    public TaskListener(TaskService taskService, CandidateService candidateService, JobService jobService) {
        this.taskService = taskService;
        this.candidateService = candidateService;
        this.jobService = jobService;
    }

    @KafkaListener(topics = CANDIDATE_TOPIC)
    public void consumeCandidateEvent(Candidate candidate) {
        candidateService.processCandidate(candidate)
            .thenAccept(result -> {
                logger.info("Candidate processed: {}", result);
            })
            .exceptionally(e -> {
                logger.error("Error processing candidate", e);
                return null;
            });
    }

    @KafkaListener(topics = JOB_TOPIC)
    public void consumeJobEvent(Job job) {
        jobService.processJob(job)
            .thenAccept(result -> {
                logger.info("Job processed: {}", result);
            })
            .exceptionally(e -> {
                logger.error("Error processing job", e);
                return null;
            });
    }

}
