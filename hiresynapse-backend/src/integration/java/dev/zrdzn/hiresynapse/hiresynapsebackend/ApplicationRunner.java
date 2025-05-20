package dev.zrdzn.hiresynapse.hiresynapsebackend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.core.Unirest;
import kong.unirest.modules.jackson.JacksonObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@Testcontainers
public class ApplicationRunner {

    private final int port = 8080;

    private ConfigurableApplicationContext context;

    @Container
    public PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer<>("postgres:latest");

    @Container
    public KafkaContainer kafkaContainer = new KafkaContainer();

    @BeforeAll
    public void beforeAll() {
        postgresqlContainer.start();
        kafkaContainer.start();
    }

    @AfterAll
    public void afterAll() {
        postgresqlContainer.stop();
        kafkaContainer.stop();
    }

    @BeforeEach
    public void beforeEach() {
        SpringApplication context = new SpringApplication(HiresynapseBackendApplication.class);

        Unirest.config()
            .setObjectMapper(
                new JacksonObjectMapper(
                    JsonMapper.builder()
                        .addModule(new JavaTimeModule())
                        .addModule(new SimpleModule())
                        .build()
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                )
            )
            .defaultBaseUrl("http://localhost:" + port + "/v1");

        context.setDefaultProperties(
            Map.of(
                "POSTGRES_URL", postgresqlContainer.getJdbcUrl(),
                "POSTGRES_USER", postgresqlContainer.getUsername(),
                "POSTGRES_PASSWORD", postgresqlContainer.getPassword(),
                "KAFKA_BOOTSTRAP_SERVERS", kafkaContainer.getBootstrapServers(),
                "AUTH0_CLIENT_ID", System.getenv("AUTH0_CLIENT_ID"),
                "AUTH0_CLIENT_SECRET", System.getenv("AUTH0_CLIENT_SECRET"),
                "AUTH0_REDIRECT_URI", System.getenv("AUTH0_REDIRECT_URI"),
                "AUTH0_ISSUER_URI", System.getenv("AUTH0_ISSUER_URI"),
                "OPENAI_API_KEY", ""
            )
        );

        context.run();
    }

    @AfterEach
    public void afterEach() {
        if (this.context != null) {
            this.context.close();
        }
    }

}
