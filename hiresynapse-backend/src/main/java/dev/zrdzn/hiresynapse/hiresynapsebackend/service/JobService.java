package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskEntityType;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class JobService {

    private final Logger logger = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final TaskService taskService;

    public JobService(JobRepository jobRepository, TaskService taskService) {
        this.jobRepository = jobRepository;
        this.taskService = taskService;
    }

    public Job initiateJobCreation(String requesterId, Job job) {
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
            try {
                logger.info("Processing job: {}", job.getId());

                return jobRepository.save(job);
            } catch (Exception e) {
                throw new RuntimeException("Error processing job", e);
            }
        });
    }

}
