package dev.zrdzn.hiresynapse.hiresynapsebackend.dto.interview;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.InterviewStatus;

public record InterviewStatusCountDto(
    InterviewStatus status,
    Long count,
    Double percentage
) {
}
