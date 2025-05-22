package dev.zrdzn.hiresynapse.hiresynapsebackend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.UserRole;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.UserService;
import kong.unirest.core.Unirest;
import kong.unirest.modules.jackson.JacksonObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@Testcontainers
public class ApplicationRunner {

    private static final int port = 8080;

    protected static ConfigurableApplicationContext context;
    protected static long defaultUserId = 1L;

    @Container
    public static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer<>("postgres:latest");

    @Container
    public static KafkaContainer kafkaContainer = new KafkaContainer();

    @BeforeAll
    public static void beforeAll() {
        postgresqlContainer.start();
        kafkaContainer.start();

        SpringApplication app = new SpringApplicationBuilder()
            .sources(
                HiresynapseBackendApplication.class,
                TestSecurityConfig.class
            )
            .profiles("test")
            .build();

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

        app.setDefaultProperties(
            Map.of(
                "spring.profiles.active", "test",
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

        context = app.run();
    }

    private static void setupFakeAuthentication() {
        UserService userService = context.getBean(UserService.class);
        User user = userService.createUser(
            "fake",
            "fake@mail.com",
            "fake",
            "user",
            UserRole.ADMIN,
            null
        );

        defaultUserId = user.getId();

        authenticateAs(defaultUserId);
    }

    protected static void authenticateAs(long userId) {
        TestSecurityConfig.TestAuthenticationFilter.setCurrentUserId(userId);
    }

    @AfterAll
    public static void afterAll() {
        postgresqlContainer.stop();
        kafkaContainer.stop();
        if (context != null) {
            context.close();
        }
    }

}
