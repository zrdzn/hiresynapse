package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record CandidateCreateDto(
    @NotBlank(message = "Email is required")
    String email,
    @NotBlank(message = "Job id is required")
    String jobId
) implements Serializable {
}
