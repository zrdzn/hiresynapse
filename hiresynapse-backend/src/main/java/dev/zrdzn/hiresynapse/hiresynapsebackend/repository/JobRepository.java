package dev.zrdzn.hiresynapse.hiresynapsebackend.repository;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByStatusAndTaskStatus(JobStatus status, TaskStatus taskStatus, Pageable pageable);

    Page<Job> findAllByTaskStatus(TaskStatus taskStatus, Pageable pageable);

    @Query(
        """
        SELECT job
        FROM Job job
        WHERE job.createdAt >= :startDate
        """
    )
    List<Job> findJobsCreatedAfter(@Param("startDate") Instant startDate);

    List<Job> findByPublishAtBefore(Instant now);

}
