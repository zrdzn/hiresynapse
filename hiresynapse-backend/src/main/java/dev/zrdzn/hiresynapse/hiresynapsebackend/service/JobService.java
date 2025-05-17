package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.JobCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.JobUpdateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.MonthlyDataDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.error.ApiError;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.JobStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogAction;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogEntityType;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.JobRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static dev.zrdzn.hiresynapse.hiresynapsebackend.shared.statistic.StatisticHelper.getMonthlyData;

@Service
@Validated
public class JobService {

    private final Logger logger = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final TaskService taskService;
    private final LogService logService;

    public JobService(
        JobRepository jobRepository,
        TaskService taskService,
        LogService logService
    ) {
        this.jobRepository = jobRepository;
        this.taskService = taskService;
        this.logService = logService;
    }

    public Job initiateJobCreation(long requesterId, @Valid JobCreateDto jobCreateDto) {
        Job createdJob = jobRepository.save(
            new Job(
                null,
                null,
                null,
                jobCreateDto.title(),
                jobCreateDto.description(),
                jobCreateDto.location(),
                jobCreateDto.salary(),
                jobCreateDto.requiredExperience(),
                jobCreateDto.status(),
                Arrays.stream(jobCreateDto.requirements().split(",")).toList(),
                Arrays.stream(jobCreateDto.benefits().split(",")).toList(),
                TaskStatus.PENDING,
                null
            )
        );

        logService.createLog(
            requesterId,
            "New job '" + createdJob.getTitle() + "' has been created",
            LogAction.CREATE,
            LogEntityType.JOB,
            createdJob.getId()
        );

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

    public void updateJob(long requesterId, long jobId, JobUpdateDto jobUpdateDto) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND, "Job not found"));

        job.setTitle(jobUpdateDto.title());
        job.setDescription(jobUpdateDto.description());
        job.setLocation(jobUpdateDto.location());
        job.setSalary(jobUpdateDto.salary());
        job.setRequiredExperience(jobUpdateDto.requiredExperience());
        job.setStatus(jobUpdateDto.status());
        job.setRequirements(Arrays.stream(jobUpdateDto.requirements().split(",")).toList());
        job.setBenefits(Arrays.stream(jobUpdateDto.benefits().split(",")).toList());

        jobRepository.save(job);

        logService.createLog(
            requesterId,
            "Job has been updated",
            LogAction.UPDATE,
            LogEntityType.JOB,
            job.getId()
        );

        logger.debug("Updated job: {}", jobId);
    }

    public void updateJobStatus(long requesterId, long jobId, JobStatus status) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND, "Job not found"));

        job.setStatus(status);

        jobRepository.save(job);

        logService.createLog(
            requesterId,
            "Job status updated to " + status,
            LogAction.UPDATE,
            LogEntityType.JOB,
            job.getId()
        );

        logger.debug("Updated job status for job: {} to {}", jobId, status);
    }

    public void updateJobTaskStatus(long jobId, TaskStatus status) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND, "Job not found"));

        job.setTaskStatus(status);

        jobRepository.save(job);

        logService.createLog(
            null,
            "Job task status updated to " + status,
            LogAction.UPDATE,
            LogEntityType.JOB,
            job.getId()
        );

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

        return getMonthlyData(jobs);
    }

    public void deleteJob(long requesterId, long jobId) {
        jobRepository.deleteById(jobId);

        logService.createLog(
            requesterId,
            "Job has been deleted",
            LogAction.DELETE,
            LogEntityType.JOB,
            jobId
        );

        logger.debug("Deleted job: {}", jobId);
    }

}
