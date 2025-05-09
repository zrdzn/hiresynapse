package dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic;

public record JobTitleCountDto(
    String title,
    Long count,
    Double percentage
) {
}
