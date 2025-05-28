package dev.zrdzn.hiresynapse.hiresynapsebackend.candidate;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.CandidateCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.CandidateStatus;
import kong.unirest.core.ContentType;
import kong.unirest.core.Empty;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CandidateControllerTest extends CandidateSpec {

    private final ObjectMapper objectMapper = context.getBean(ObjectMapper.class);

    @Test
    void testCreateCandidate() throws IOException {
        authenticateAs(defaultUserId);

        String email = "newcandidate@example.com";
        CandidateCreateDto request = new CandidateCreateDto(
            email,
            job.getId(),
            "direct"
        );

        HttpResponse<Candidate> response = Unirest.post("/candidates")
            .field("dto", objectMapper.writeValueAsString(request), "application/json")
            .field("file", new ByteArrayResource(resumeFile.getInputStream().readAllBytes()).getByteArray(), ContentType.create("application/pdf"), "resume.pdf")
            .asObject(Candidate.class);

        assertEquals(200, response.getStatus());
        assertEquals(email, response.getBody().getEmail());
        assertEquals(CandidateStatus.PENDING, response.getBody().getStatus());
    }

    @Test
    void testGetCandidates() {
        authenticateAs(defaultUserId);

        HttpResponse<List> response = Unirest.get("/candidates")
            .asObject(List.class);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetPendingCandidates() {
        authenticateAs(defaultUserId);

        HttpResponse<List> response = Unirest.get("/candidates/pending")
            .asObject(List.class);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetCandidate() {
        authenticateAs(defaultUserId);

        HttpResponse<Candidate> response = Unirest.get("/candidates/{id}")
            .routeParam("id", String.valueOf(candidate.getId()))
            .asObject(Candidate.class);

        assertEquals(200, response.getStatus());
        assertEquals(candidate.getId(), response.getBody().getId());
        assertEquals(candidate.getEmail(), response.getBody().getEmail());
    }

    @Test
    void testRejectCandidate() {
        authenticateAs(defaultUserId);

        HttpResponse<Empty> response = Unirest.patch("/candidates/{id}/reject")
            .routeParam("id", String.valueOf(candidate.getId()))
            .asEmpty();

        assertEquals(200, response.getStatus());

        HttpResponse<Candidate> checkResponse = Unirest.get("/candidates/{id}")
            .routeParam("id", String.valueOf(candidate.getId()))
            .asObject(Candidate.class);

        assertEquals(CandidateStatus.REJECTED, checkResponse.getBody().getStatus());
    }

} 