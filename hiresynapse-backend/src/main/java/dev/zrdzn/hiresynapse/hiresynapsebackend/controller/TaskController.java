package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.Task;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/queues")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{status}")
    public List<Task> getPendingTasks(@PathVariable TaskStatus status) {
        return taskService.getTasks(status);
    }

}
