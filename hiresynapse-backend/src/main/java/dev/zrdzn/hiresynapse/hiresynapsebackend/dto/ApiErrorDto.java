package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

public record ApiErrorDto(
    String status,
    int code,
    String description
) {
}
