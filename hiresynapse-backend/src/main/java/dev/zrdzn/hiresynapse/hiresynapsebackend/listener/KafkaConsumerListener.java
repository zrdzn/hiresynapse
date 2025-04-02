package dev.zrdzn.hiresynapse.hiresynapsebackend.listener;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.CandidateService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.JobService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.CANDIDATE_TOPIC;
import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.JOB_TOPIC;

@Service
public class KafkaConsumerListener {

    private final CandidateService candidateService;
    private final JobService jobService;

    public KafkaConsumerListener(CandidateService candidateService, JobService jobService) {
        this.candidateService = candidateService;
        this.jobService = jobService;
    }

    @KafkaListener(topics = CANDIDATE_TOPIC)
    public void consumeCandidateEvent(Candidate candidate) {
        System.out.println("Consumed candidate event: " + candidate);
    }

    @KafkaListener(topics = JOB_TOPIC)
    public void consumeJobEvent(Job job) {
        System.out.println("Consumed job event: " + job);
    }


}
