package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewType;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.Instant;

public record InterviewCreateDto(
    @NotBlank(message = "Candidate id is required")
    String candidateId,
    Instant interviewAt,
    InterviewType interviewType,
    String notes,
    boolean enableQuestions,
    int questionsAmount
) implements Serializable {
}
