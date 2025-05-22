package dev.zrdzn.hiresynapse.hiresynapsebackend.job;

import dev.zrdzn.hiresynapse.hiresynapsebackend.ApplicationRunner;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.JobCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.JobStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.JobService;
import org.junit.jupiter.api.BeforeEach;

public class JobSpec extends ApplicationRunner {

    protected final JobService jobService = context.getBean(JobService.class);

    protected Job job;

    @BeforeEach
    public void setupJobs() {
        job = jobService.initiateJobCreation(
            defaultUserId,
            new JobCreateDto(
                "Test Job",
                "This is a test job description.",
                "remote",
                "2000$ - 4000$",
                "10 years",
                JobStatus.SCHEDULED,
                "Spring, Java, Hibernate",
                "Free parking, coffee, and snacks provided."
            )
        );
    }

}
