package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserPrincipal;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.task.Task;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.TaskService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks(
        @AuthenticationPrincipal UserPrincipal principal,
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return taskService.getTasks(pageable).getContent();
    }

}
