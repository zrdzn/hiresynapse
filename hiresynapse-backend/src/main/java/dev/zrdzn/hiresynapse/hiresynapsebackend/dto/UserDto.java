package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

public record UserDto(
    String id,
    String username,
    String email,
    String role
) {
}
