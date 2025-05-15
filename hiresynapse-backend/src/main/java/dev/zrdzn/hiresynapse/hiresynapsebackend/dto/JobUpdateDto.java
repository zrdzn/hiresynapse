package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.job.JobStatus;

public record JobUpdateDto(
    String title,
    String description,
    String location,
    String salary,
    String requiredExperience,
    JobStatus status,
    String requirements,
    String benefits
) {
}
