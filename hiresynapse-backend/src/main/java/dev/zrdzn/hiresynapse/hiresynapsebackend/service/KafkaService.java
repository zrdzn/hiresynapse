package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.CANDIDATE_TOPIC;
import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.JOB_TOPIC;

@Service
public class KafkaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCandidateEvent(Candidate candidate) {
        kafkaTemplate.send(CANDIDATE_TOPIC, String.valueOf(candidate.getId()), candidate);
    }

    public void sendJobEvent(Job job) {
        kafkaTemplate.send(JOB_TOPIC, String.valueOf(job.getId()), job);
    }

}
