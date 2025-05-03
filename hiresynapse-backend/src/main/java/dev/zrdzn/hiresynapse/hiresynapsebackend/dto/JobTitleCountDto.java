package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

public record JobTitleCountDto(
    String title,
    Long count,
    double percentage
) {
}
