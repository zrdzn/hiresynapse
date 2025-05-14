package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogAction;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogEntityType;

import java.time.Instant;

public record LogDto(
    long id,
    Instant createdAt,
    Instant updatedAt,
    Long performerId,
    String performerName,
    String description,
    LogAction action,
    LogEntityType entityType,
    long entityId
) {
}