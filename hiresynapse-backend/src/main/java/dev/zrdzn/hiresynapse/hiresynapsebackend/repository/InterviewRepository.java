package dev.zrdzn.hiresynapse.hiresynapsebackend.repository;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Interview;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterviewRepository extends MongoRepository<Interview, String> {

    Page<Interview> findAllByTaskStatus(TaskStatus taskStatus, Pageable pageable);

    List<Interview> findTop3ByStatusAndTaskStatusOrderByInterviewAtAsc(InterviewStatus interviewStatus, TaskStatus taskStatus);

}
