package dev.zrdzn.hiresynapse.hiresynapsebackend.job;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.JobCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.JobUpdateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.JobStatus;
import kong.unirest.core.Empty;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void testGetJobs() {
        authenticateAs(defaultUserId);

        HttpResponse<List> response = Unirest.get("/jobs")
            .asObject(List.class);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetPublishedJobs() {
        authenticateAs(defaultUserId);

        HttpResponse<List> response = Unirest.get("/jobs/published")
            .asObject(List.class);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetJob() {
        authenticateAs(defaultUserId);

        HttpResponse<Job> response = Unirest.get("/jobs/{id}")
            .routeParam("id", String.valueOf(job.getId()))
            .asObject(Job.class);

        assertEquals(200, response.getStatus());
        assertEquals(job.getId(), response.getBody().getId());
        assertEquals(job.getTitle(), response.getBody().getTitle());
    }

    @Test
    void testUpdateJob() {
        authenticateAs(defaultUserId);

        String updatedTitle = "Updated Job Title";
        String updatedDescription = "Updated job description";
        
        JobUpdateDto request = new JobUpdateDto(
            updatedTitle,
            updatedDescription,
            "hybrid",
            "3000$ - 5000$",
            "5 years",
            JobStatus.UNPUBLISHED,
            "Java, Spring Boot, PostgreSQL",
            "Health insurance, gym membership"
        );

        HttpResponse<Empty> response = Unirest.patch("/jobs/{id}")
            .routeParam("id", String.valueOf(job.getId()))
            .contentType("application/json")
            .body(request)
            .asEmpty();

        assertEquals(200, response.getStatus());

        // Verify the update
        HttpResponse<Job> checkResponse = Unirest.get("/jobs/{id}")
            .routeParam("id", String.valueOf(job.getId()))
            .asObject(Job.class);

        assertEquals(updatedTitle, checkResponse.getBody().getTitle());
        assertEquals(updatedDescription, checkResponse.getBody().getDescription());
    }

    @Test
    void testPublishJob() {
        authenticateAs(defaultUserId);

        HttpResponse<Empty> response = Unirest.patch("/jobs/{id}/publish")
            .routeParam("id", String.valueOf(job.getId()))
            .asEmpty();

        assertEquals(200, response.getStatus());

        // Verify the status change
        HttpResponse<Job> checkResponse = Unirest.get("/jobs/{id}")
            .routeParam("id", String.valueOf(job.getId()))
            .asObject(Job.class);

        assertEquals(JobStatus.PUBLISHED, checkResponse.getBody().getStatus());
    }

    @Test
    void testUnpublishJob() {
        authenticateAs(defaultUserId);

        // First publish the job
        Unirest.patch("/jobs/{id}/publish")
            .routeParam("id", String.valueOf(job.getId()))
            .asEmpty();

        // Then unpublish it
        HttpResponse<Empty> response = Unirest.patch("/jobs/{id}/unpublish")
            .routeParam("id", String.valueOf(job.getId()))
            .asEmpty();

        assertEquals(200, response.getStatus());

        // Verify the status change
        HttpResponse<Job> checkResponse = Unirest.get("/jobs/{id}")
            .routeParam("id", String.valueOf(job.getId()))
            .asObject(Job.class);

        assertEquals(JobStatus.UNPUBLISHED, checkResponse.getBody().getStatus());
    }

    @Test
    void testScheduleJob() {
        authenticateAs(defaultUserId);

        Instant publishAt = Instant.now().plusSeconds(3600); // Schedule for 1 hour later

        HttpResponse<Empty> response = Unirest.patch("/jobs/{id}/schedule/{publishAt}")
            .routeParam("id", String.valueOf(job.getId()))
            .routeParam("publishAt", publishAt.toString())
            .asEmpty();

        assertEquals(200, response.getStatus());

        // Verify the schedule
        HttpResponse<Job> checkResponse = Unirest.get("/jobs/{id}")
            .routeParam("id", String.valueOf(job.getId()))
            .asObject(Job.class);

        assertEquals(JobStatus.SCHEDULED, checkResponse.getBody().getStatus());
        assertNotNull(checkResponse.getBody().getPublishAt());
    }

    @Test
    void testCancelSchedule() {
        authenticateAs(defaultUserId);

        // First schedule the job
        Instant publishAt = Instant.now().plusSeconds(3600);
        Unirest.patch("/jobs/{id}/schedule/{publishAt}")
            .routeParam("id", String.valueOf(job.getId()))
            .routeParam("publishAt", publishAt.toString())
            .asEmpty();

        // Then cancel the schedule
        HttpResponse<Empty> response = Unirest.patch("/jobs/{id}/cancel-schedule")
            .routeParam("id", String.valueOf(job.getId()))
            .asEmpty();

        assertEquals(200, response.getStatus());

        // Verify the cancellation
        HttpResponse<Job> checkResponse = Unirest.get("/jobs/{id}")
            .routeParam("id", String.valueOf(job.getId()))
            .asObject(Job.class);

        assertEquals(JobStatus.UNPUBLISHED, checkResponse.getBody().getStatus());
    }

    @Test
    void testDeleteJob() {
        authenticateAs(defaultUserId);

        HttpResponse<Empty> response = Unirest.delete("/jobs/{id}")
            .routeParam("id", String.valueOf(job.getId()))
            .asEmpty();

        assertEquals(200, response.getStatus());

        // Verify the deletion
        HttpResponse<Job> checkResponse = Unirest.get("/jobs/{id}")
            .routeParam("id", String.valueOf(job.getId()))
            .asObject(Job.class);

        assertEquals(404, checkResponse.getStatus());
    }

}
