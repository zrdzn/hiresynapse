package dev.zrdzn.hiresynapse.hiresynapsebackend.user;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerTest extends UserSpec {

    @Test
    void testGetUsers() {
        authenticateAs(defaultUserId);

        HttpResponse<List> response = Unirest.get("/users")
            .asObject(List.class);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
    }

} 