package dev.zrdzn.hiresynapse.hiresynapsebackend.repository;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
}
