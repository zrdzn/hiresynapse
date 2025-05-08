package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.MonthlyDataDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.error.ApiError;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.JobStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.JobRepository;
import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.stat.StatHelper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Validated
public class JobService {

    private final Logger logger = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final TaskService taskService;

    public JobService(
        JobRepository jobRepository,
        TaskService taskService
    ) {
        this.jobRepository = jobRepository;
        this.taskService = taskService;
    }

    public Job initiateJobCreation(@Valid Job job) {
        job.setTaskStatus(TaskStatus.PENDING);

        Job createdJob = jobRepository.save(job);

        taskService.sendEvent(
            createdJob.getId(),
            createdJob
        );

        logger.debug("Started job creation task for job: {}", createdJob.getId());

        return createdJob;
    }

    public CompletableFuture<Job> processJob(Job job) {
        return CompletableFuture.supplyAsync(() -> {
            logger.debug("Processing job: {}", job.getId());

            updateJobTaskStatus(job.getId(), TaskStatus.COMPLETED);

            return job;
        }).exceptionally(exception -> {
            logger.error("Error processing job", exception);

            updateJobTaskStatus(job.getId(), TaskStatus.FAILED);

            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to process job");
        });
    }

    public void updateJobStatus(long jobId, JobStatus status) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND, "Job not found"));

        job.setStatus(status);

        jobRepository.save(job);

        logger.debug("Updated job status for job: {} to {}", jobId, status);
    }

    public void updateJobTaskStatus(long jobId, TaskStatus status) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND, "Job not found"));

        job.setTaskStatus(status);

        jobRepository.save(job);

        logger.debug("Updated job task status for job: {} to {}", jobId, status);
    }

    public List<Job> getJobs(Pageable pageable) {
        return jobRepository.findAllByTaskStatus(TaskStatus.COMPLETED, pageable).getContent();
    }

    public List<Job> getPublishedJobs(Pageable pageable) {
        return jobRepository.findByStatusAndTaskStatus(JobStatus.PUBLISHED, TaskStatus.COMPLETED, pageable).getContent();
    }

    public Optional<Job> getJob(long jobId) {
        return jobRepository.findById(jobId);
    }

    public int getJobCount() {
        return (int) jobRepository.count();
    }

    public MonthlyDataDto getJobsFromLastSixMonths() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        Instant startDate = sixMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Job> jobs = jobRepository.findJobsCreatedAfter(startDate);

        Map<String, Integer> monthlyData = StatHelper.countByMonth(jobs);
        double growthRate = StatHelper.calculateGrowthRate(monthlyData);

        return new MonthlyDataDto(
            jobs.size(),
            growthRate,
            monthlyData
        );
    }

    public void deleteJob(long jobId) {
        jobRepository.deleteById(jobId);
        logger.debug("Deleted job: {}", jobId);
    }

}
