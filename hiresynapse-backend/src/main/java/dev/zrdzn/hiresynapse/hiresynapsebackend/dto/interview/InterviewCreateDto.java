package dev.zrdzn.hiresynapse.hiresynapsebackend.dto.interview;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.InterviewStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.InterviewType;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.Instant;

public record InterviewCreateDto(
    @NotBlank(message = "Candidate id is required")
    long candidateId,
    Instant interviewAt,
    InterviewType interviewType,
    InterviewStatus interviewStatus,
    String notes,
    boolean enableQuestions,
    int questionsAmount
) implements Serializable {
}
