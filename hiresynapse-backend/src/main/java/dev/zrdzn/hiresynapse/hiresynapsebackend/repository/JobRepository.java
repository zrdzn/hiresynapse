package dev.zrdzn.hiresynapse.hiresynapsebackend.repository;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Job;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.JobStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<Job, String> {

    Page<Job> findByStatusAndTaskStatus(JobStatus status, TaskStatus taskStatus, Pageable pageable);

    Page<Job> findAllByTaskStatus(TaskStatus taskStatus, Pageable pageable);

}
