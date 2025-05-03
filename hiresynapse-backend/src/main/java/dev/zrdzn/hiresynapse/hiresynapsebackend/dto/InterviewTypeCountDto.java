package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewType;

public record InterviewTypeCountDto(
    InterviewType interviewType,
    Long count,
    Double percentage
) {
}
