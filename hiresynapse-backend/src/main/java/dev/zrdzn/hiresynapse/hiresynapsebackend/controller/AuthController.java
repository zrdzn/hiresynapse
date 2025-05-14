package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.UserPrincipal;
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
        return ResponseEntity.ok(
            User.builder()
                .id(principal.getUser().getId())
                .username(principal.getUser().getUsername())
                .email(principal.getUser().getEmail())
                .firstName(principal.getUser().getFirstName())
                .lastName(principal.getUser().getLastName())
                .role(principal.getUser().getRole())
                .pictureUrl(principal.getUser().getPictureUrl())
                .build()
        );
    }



}
