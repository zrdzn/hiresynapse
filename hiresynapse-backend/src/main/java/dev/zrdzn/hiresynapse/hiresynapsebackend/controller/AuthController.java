package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<User> getMe(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(new User(
            principal.getUser().getId(),
            principal.getUser().getUsername(),
            principal.getUser().getEmail(),
            principal.getUser().getFirstName(),
            principal.getUser().getLastName(),
            principal.getUser().getRole(),
            principal.getUser().getPictureUrl()
        ));
    }

}
