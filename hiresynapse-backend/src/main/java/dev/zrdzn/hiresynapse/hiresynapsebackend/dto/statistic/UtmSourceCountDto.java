package dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic;

public record UtmSourceCountDto(
    String source,
    Long count,
    Double percentage
) {
}
