package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.MonthlyDataDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.JobStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.JobRepository;
import dev.zrdzn.hiresynapse.hiresynapsebackend.shared.stat.StatHelper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
@Validated
public class JobService {

    private final Logger logger = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final TaskService taskService;
    private final MongoTemplate mongoTemplate;

    public JobService(JobRepository jobRepository, TaskService taskService, MongoTemplate mongoTemplate) {
        this.jobRepository = jobRepository;
        this.taskService = taskService;
        this.mongoTemplate = mongoTemplate;
    }

    public Job initiateJobCreation(@Valid Job job) {
        job.setTaskStatus(TaskStatus.PENDING);

        Job createdJob = jobRepository.save(job);

        taskService.sendEvent(
            createdJob.getId(),
            createdJob
        );

        logger.info("Started job creation task for job: {}", createdJob.getId());

        return createdJob;
    }

    public CompletableFuture<Job> processJob(Job job) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Processing job: {}", job.getId());

            updateJobTaskStatus(job.getId(), TaskStatus.COMPLETED);

            return job;
        }).exceptionally(e -> {
            logger.error("Error processing job", e);

            updateJobTaskStatus(job.getId(), TaskStatus.FAILED);

            throw new CompletionException(e);
        });
    }

    public void updateJobStatus(String jobId, JobStatus status) {
        Query query = new Query(Criteria.where("_id").is(jobId));
        Update update = new Update().set("status", status);
        mongoTemplate.updateFirst(query, update, Job.class);

        logger.info("Updated job status for job: {} to {}", jobId, status);
    }

    public void updateJobTaskStatus(String jobId, TaskStatus status) {
        Query query = new Query(Criteria.where("_id").is(jobId));
        Update update = new Update().set("taskStatus", status);
        mongoTemplate.updateFirst(query, update, Job.class);

        logger.info("Updated job task status for job: {} to {}", jobId, status);
    }

    public List<Job> getJobs(String requesterId, Pageable pageable) {
        return jobRepository.findAllByTaskStatus(TaskStatus.COMPLETED, pageable).getContent();
    }

    public List<Job> getPublishedJobs(Pageable pageable) {
        return jobRepository.findByStatusAndTaskStatus(JobStatus.PUBLISHED, TaskStatus.COMPLETED, pageable).getContent();
    }

    public Optional<Job> getJob(String jobId) {
        return jobRepository.findById(jobId);
    }

    public int getJobCount() {
        return (int) jobRepository.count();
    }

    public MonthlyDataDto getJobsFromLastSixMonths() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        Instant startDate = sixMonthsAgo.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Query query = new Query();
        query.addCriteria(Criteria.where("createdAt").gte(startDate));

        List<Job> jobs = mongoTemplate.find(query, Job.class);

        Map<String, Integer> monthlyData = StatHelper.countByMonth(jobs);
        double growthRate = StatHelper.calculateGrowthRate(monthlyData);

        return new MonthlyDataDto(
            jobs.size(),
            growthRate,
            monthlyData
        );
    }

    public void deleteJob(String jobId) {
        jobRepository.deleteById(jobId);
        logger.info("Deleted job: {}", jobId);
    }

}
