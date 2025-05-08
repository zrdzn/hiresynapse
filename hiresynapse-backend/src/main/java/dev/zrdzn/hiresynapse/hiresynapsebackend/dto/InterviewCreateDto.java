package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewType;
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
