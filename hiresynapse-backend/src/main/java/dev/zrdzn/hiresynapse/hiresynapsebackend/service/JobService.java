package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.JobRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class JobService {

    private final Logger logger = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final TaskService taskService;

    public JobService(JobRepository jobRepository, TaskService taskService) {
        this.jobRepository = jobRepository;
        this.taskService = taskService;
    }

    public Job initiateJobCreation(String requesterId, @Valid Job job) {
        Job createdJob = jobRepository.save(job);

        taskService.createTaskAndDispatch(
            TaskStatus.PENDING,
            createdJob,
            requesterId,
            null
        );

        logger.info("Started job creation task for job: {}", createdJob.getId());

        return createdJob;
    }

    public CompletableFuture<Job> processJob(Job job) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Processing job: {}", job.getId());

            return job;
        }).exceptionally(e -> {
            logger.error("Error processing job", e);

            throw new CompletionException(e);
        });
    }

    public List<Job> getJobs(String requesterId, Pageable pageable) {
        return jobRepository.findAll(pageable).getContent();
    }

}
