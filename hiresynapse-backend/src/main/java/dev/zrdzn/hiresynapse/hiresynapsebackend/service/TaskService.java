package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.CANDIDATE_TOPIC;
import static dev.zrdzn.hiresynapse.hiresynapsebackend.config.KafkaConfig.JOB_TOPIC;

@Service
public class TaskService {

    private final Map<Class<?>, String> topicMappings = Map.of(
        Candidate.class, CANDIDATE_TOPIC,
        Job.class, JOB_TOPIC
    );

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TaskService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public <T> void sendEvent(String entityId, T entity) {
        String topic = topicMappings.get(entity.getClass());
        if (topic == null) {
            throw new IllegalArgumentException("No topic mapping for: " + entity.getClass().getName());
        }

        kafkaTemplate.send(topic, entityId, entity);
    }

}
