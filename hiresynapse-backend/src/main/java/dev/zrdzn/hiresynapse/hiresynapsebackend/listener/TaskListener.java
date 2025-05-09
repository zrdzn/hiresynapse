package dev.zrdzn.hiresynapse.hiresynapsebackend.listener;

import dev.zrdzn.hiresynapse.hiresynapsebackend.event.CandidateCreateEvent;
import dev.zrdzn.hiresynapse.hiresynapsebackend.event.InterviewCreateEvent;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.CandidateService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.InterviewService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.CANDIDATE_TOPIC;
import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.INTERVIEW_TOPIC;
import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.JOB_TOPIC;

@Service
public class TaskListener {

    private final Logger logger = LoggerFactory.getLogger(TaskListener.class);

    private final CandidateService candidateService;
    private final JobService jobService;
    private final InterviewService interviewService;

    public TaskListener(CandidateService candidateService, JobService jobService, InterviewService interviewService) {
        this.candidateService = candidateService;
        this.jobService = jobService;
        this.interviewService = interviewService;
    }

    @KafkaListener(topics = CANDIDATE_TOPIC)
    public void consumeCandidateEvent(CandidateCreateEvent candidateCreateEvent) {
        candidateService.processCandidate(candidateCreateEvent)
            .thenAccept(result -> {
                logger.debug("Candidate processed: {}", result);
            })
            .exceptionally(exception -> {
                logger.error("Error processing candidate", exception);
                return null;
            });
    }

    @KafkaListener(topics = JOB_TOPIC)
    public void consumeJobEvent(Job job) {
        jobService.processJob(job)
            .thenAccept(result -> {
                logger.debug("Job processed: {}", result);
            })
            .exceptionally(exception -> {
                logger.error("Error processing job", exception);
                return null;
            });
    }

    @KafkaListener(topics = INTERVIEW_TOPIC)
    public void consumeInterviewEvent(InterviewCreateEvent interviewCreateEvent) {
        interviewService.processInterview(interviewCreateEvent)
            .thenAccept(result -> {
                logger.debug("Interview processed: {}", result);
            })
            .exceptionally(exception -> {
                logger.error("Error processing interview", exception);
                return null;
            });
    }

}
