package dev.zrdzn.hiresynapse.hiresynapsebackend.job;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.JobCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.JobStatus;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobControllerTest extends JobSpec {

    @Test
    void testCreateJob() {
        authenticateAs(defaultUserId);

        String name = "Test Job";
        String description = "This is a test job description.";

        JobCreateDto request = new JobCreateDto(
            name,
            description,
            "remote",
            "2000$ - 4000$",
            "10 years",
            JobStatus.SCHEDULED,
            "Spring, Java, Hibernate",
            "Free parking, coffee, and snacks provided."
        );

        HttpResponse<Job> response = Unirest.post("/jobs")
            .contentType("application/json")
            .body(request)
            .asObject(Job.class);

        assertEquals(200, response.getStatus());
        assertEquals(name, response.getBody().getTitle());
        assertEquals(description, response.getBody().getDescription());
    }

}
