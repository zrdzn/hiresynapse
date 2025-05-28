package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.JobCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.JobUpdateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.JobStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.UserPrincipal;
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

import java.time.Instant;
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
    public Job createJob(
        @AuthenticationPrincipal UserPrincipal principal,
        @RequestBody JobCreateDto jobCreateDto
    ) {
        return jobService.initiateJobCreation(principal.getUser().getId(), jobCreateDto);
    }

    @PatchMapping("/{jobId}")
    public void updateJob(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long jobId,
        @RequestBody JobUpdateDto jobUpdateDto
    ) {
        jobService.updateJob(principal.getUser().getId(), jobId, jobUpdateDto);
    }

    @GetMapping
    public List<Job> getJobs(
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return jobService.getJobs(pageable);
    }

    @GetMapping("/published")
    public List<Job> getPublishedJobs(
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return jobService.getPublishedJobs(pageable);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJob(
        @PathVariable long jobId
    ) {
        Optional<Job> job = jobService.getJob(jobId);

        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{jobId}/publish")
    public void publishJob(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long jobId
    ) {
        jobService.updateJobStatus(principal.getUser().getId(), jobId, JobStatus.PUBLISHED);
    }

    @PatchMapping("/{jobId}/unpublish")
    public void unpublishJob(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long jobId
    ) {
        jobService.updateJobStatus(principal.getUser().getId(), jobId, JobStatus.UNPUBLISHED);
    }

    @PatchMapping("/{jobId}/schedule/{publishAt}")
    public void scheduleJob(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long jobId,
        @PathVariable Instant publishAt
    ) {
        jobService.updateJobPublishAt(principal.getUser().getId(), jobId, publishAt);
    }

    @PatchMapping("/{jobId}/cancel-schedule")
    public void cancelJobSchedule(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long jobId
    ) {
        jobService.cancelJobSchedule(principal.getUser().getId(), jobId);
    }

    @DeleteMapping("/{jobId}")
    public void deleteJob(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long jobId
    ) {
        jobService.deleteJob(principal.getUser().getId(), jobId);
    }

}
