package dev.zrdzn.hiresynapse.hiresynapsebackend.user;

import dev.zrdzn.hiresynapse.hiresynapsebackend.ApplicationRunner;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.UserRole;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;

import java.time.Instant;

public class UserSpec extends ApplicationRunner {

    protected final UserService userService = context.getBean(UserService.class);

    protected User testUser;

    @BeforeEach
    public void setupUsers() {
        String uniqueEmail = "testuser" + Instant.now().toEpochMilli() + "@example.com";
        testUser = userService.createUser(
            "testuser",
            uniqueEmail,
            "Test",
            "User",
            UserRole.RECRUITER,
            "https://example.com/avatar.jpg"
        );
    }
} 