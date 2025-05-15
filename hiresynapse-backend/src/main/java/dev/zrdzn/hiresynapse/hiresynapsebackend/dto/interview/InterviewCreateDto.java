package dev.zrdzn.hiresynapse.hiresynapsebackend.dto.interview;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.InterviewStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.InterviewType;

import java.io.Serializable;
import java.time.Instant;

public record InterviewCreateDto(
    long candidateId,
    Instant interviewAt,
    InterviewType interviewType,
    InterviewStatus interviewStatus,
    String notes,
    boolean enableQuestions,
    int questionsAmount
) implements Serializable {
}
