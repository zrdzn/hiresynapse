package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record CandidateCreateDto(
    @NotBlank(message = "Email is required")
    String email,
    @NotNull(message = "Job id is required")
    Long jobId,
    String utmSource
) implements Serializable {
}
