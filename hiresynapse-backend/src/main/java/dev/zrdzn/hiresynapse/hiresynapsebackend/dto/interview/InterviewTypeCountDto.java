package dev.zrdzn.hiresynapse.hiresynapsebackend.dto.interview;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.InterviewType;

public record InterviewTypeCountDto(
    InterviewType interviewType,
    Long count,
    Double percentage
) {
}
