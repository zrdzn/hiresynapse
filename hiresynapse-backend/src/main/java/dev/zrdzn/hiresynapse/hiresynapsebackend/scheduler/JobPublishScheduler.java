package dev.zrdzn.hiresynapse.hiresynapsebackend.scheduler;

import dev.zrdzn.hiresynapse.hiresynapsebackend.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class JobPublishScheduler {

    private final Logger logger = LoggerFactory.getLogger(JobPublishScheduler.class);

    private final JobService jobService;

    public JobPublishScheduler(JobService jobService) {
        this.jobService = jobService;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void executeScheduledJobs() {
        logger.debug("Executing scheduled jobs");
        jobService.executeScheduledJobs();
    }

}
