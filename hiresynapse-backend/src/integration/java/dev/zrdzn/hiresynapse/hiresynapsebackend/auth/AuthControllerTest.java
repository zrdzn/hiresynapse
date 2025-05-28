package dev.zrdzn.hiresynapse.hiresynapsebackend.auth;

import dev.zrdzn.hiresynapse.hiresynapsebackend.ApplicationRunner;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.User;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthControllerTest extends ApplicationRunner {

    @Test
    void testGetMe() {
        authenticateAs(defaultUserId);

        HttpResponse<User> response = Unirest.get("/auth/me")
            .asObject(User.class);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
        assertEquals(defaultUserId, response.getBody().getId());
    }

} 