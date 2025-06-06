package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.UserPrincipal;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return userService.getUsers(pageable);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long userId
    ) {
        userService.deleteUser(principal.getUser().getId(), userId);
    }

}
