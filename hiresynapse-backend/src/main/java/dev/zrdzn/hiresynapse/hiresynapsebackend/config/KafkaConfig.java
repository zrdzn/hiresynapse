package dev.zrdzn.hiresynapse.hiresynapsebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {

    public static final String CANDIDATE_TOPIC = "candidates";
    public static final String JOB_TOPIC = "jobs";

}
