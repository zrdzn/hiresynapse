package dev.zrdzn.hiresynapse.hiresynapsebackend.repository;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Interview;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterviewRepository extends MongoRepository<Interview, String> {

    Page<Interview> findAllByTaskStatus(TaskStatus taskStatus, Pageable pageable);

    Page<Interview> findByStatusAndTaskStatusOrderByInterviewAtAsc(InterviewStatus interviewStatus, TaskStatus taskStatus, Pageable pageable);

}
