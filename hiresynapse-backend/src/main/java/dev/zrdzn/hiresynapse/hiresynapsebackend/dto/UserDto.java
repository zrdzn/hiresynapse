package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserRole;

public record UserDto(
    String id,
    String username,
    String email,
    UserRole role,
    String pictureUrl
) {
}
