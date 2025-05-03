package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.JobStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserPrincipal;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.JobService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.initiateJobCreation(job);
    }

    @GetMapping
    public List<Job> getJobs(
        @AuthenticationPrincipal UserPrincipal principal,
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return jobService.getJobs(principal.getUser().getId(), pageable);
    }

    @GetMapping("/published")
    public List<Job> getPublishedJobs(
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return jobService.getPublishedJobs(pageable);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJob(
        @PathVariable String jobId
    ) {
        Optional<Job> job = jobService.getJob(jobId);

        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{jobId}/publish")
    public void updateJobStatus(@PathVariable String jobId) {
        jobService.updateJobStatus(jobId, JobStatus.PUBLISHED);
    }

    @PatchMapping("/{jobId}/unpublish")
    public void unpublishJob(
        @PathVariable String jobId) {
        jobService.updateJobStatus(jobId, JobStatus.UNPUBLISHED);
    }

    @DeleteMapping("/{jobId}")
    public void deleteJob(
        @PathVariable String jobId) {
        jobService.deleteJob(jobId);
    }

}
