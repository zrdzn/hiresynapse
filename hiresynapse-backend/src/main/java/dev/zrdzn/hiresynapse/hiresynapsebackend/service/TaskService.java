package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.Task;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskEntity;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskEventDispatchService taskEventDispatchService;

    public TaskService(TaskRepository taskRepository, TaskEventDispatchService taskEventDispatchService) {
        this.taskRepository = taskRepository;
        this.taskEventDispatchService = taskEventDispatchService;
    }

    public <T extends TaskEntity> void createTaskAndDispatch(TaskStatus status, T entity, String initiatorId, String initiatorEmail) {
        if (entity.getType() == null) {
            throw new IllegalArgumentException("Task entity type cannot be null");
        }

        if (entity.getId() == null) {
            throw new IllegalArgumentException("Task entity ID cannot be null");
        }

        logger.info("Creating task for entity: {} with ID: {}", entity.getType(), entity.getId());

        taskRepository.save(new Task(null, status, entity, initiatorId, initiatorEmail));

        taskEventDispatchService.sendEvent(entity);
    }

    public void updateTaskStatus(String taskId, TaskStatus status) {
        logger.info("Updating task status for task ID: {} to status: {}", taskId, status);

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        taskRepository.save(task);
    }

    public Page<Task> getTasks(Pageable pageable) {
        logger.info("Fetching {} tasks", pageable.getPageSize());

        return taskRepository.findAll(pageable);
    }

}
