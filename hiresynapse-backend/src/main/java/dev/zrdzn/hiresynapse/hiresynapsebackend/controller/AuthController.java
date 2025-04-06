package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.UserDto;
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
    public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(new UserDto(principal.getUser().id(), principal.getUser().username(), principal.getUser().email(), principal.getUser().role(), principal.getUser().pictureUrl()));
    }

}
