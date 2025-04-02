package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.ProcessStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final Logger logger = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final KafkaService kafkaService;

    public JobService(JobRepository jobRepository, KafkaService kafkaService) {
        this.jobRepository = jobRepository;
        this.kafkaService = kafkaService;
    }

    public Job initiateJobCreation(Job job) {
        job.setProcessStatus(ProcessStatus.PENDING);

        Job createdJob = jobRepository.save(job);

        logger.info("({}) Started job creation process for job: {}", createdJob.getProcessStatus(), createdJob.getId());

        kafkaService.sendJobEvent(job);

        return createdJob;
    }

    public Job processJob(Job job) {
        // influx
        // ai

        job.setProcessStatus(ProcessStatus.PROCESSED);
        Job processedJob = jobRepository.save(job);

        logger.info("({}) Processed job: {}", processedJob.getProcessStatus(), processedJob.getId());

        return processedJob;
    }

}
