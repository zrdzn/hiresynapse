package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

public record UtmSourceCountDto(
    String source,
    Long count,
    Double percentage
) {
}
