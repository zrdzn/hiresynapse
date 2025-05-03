package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewStatus;

public record InterviewStatusCountDto(
    InterviewStatus status,
    Long count,
    Double percentage
) {
}
