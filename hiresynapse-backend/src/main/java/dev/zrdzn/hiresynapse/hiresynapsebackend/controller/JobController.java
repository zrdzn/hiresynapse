package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.JobStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserPrincipal;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.JobService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public Job createJob(@AuthenticationPrincipal UserPrincipal principal, @RequestBody Job job) {
        return jobService.initiateJobCreation(job);
    }

    @GetMapping
    public List<Job> getJobs(
        @AuthenticationPrincipal UserPrincipal principal,
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return jobService.getJobs(principal.getUser().id(), pageable);
    }

    @PatchMapping("/{jobId}/{status}")
    public void updateJobStatus(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable String jobId,
        @PathVariable JobStatus status) {
        jobService.updateJobStatus(jobId, status);
    }

}
