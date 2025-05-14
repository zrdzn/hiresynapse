package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.LogDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.Log;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogAction;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogEntityType;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.LogRepository;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class LogService {

    private final Logger logger = LoggerFactory.getLogger(LogService.class);

    private final LogRepository logRepository;
    private final UserService userService;

    public LogService(
        LogRepository logRepository,
        UserService userService
    ) {
        this.logRepository = logRepository;
        this.userService = userService;
    }

    public LogDto createLog(@Nullable Long performerId, String description, LogAction action, LogEntityType entityType, long entityId) {
        User performer = null;
        if (performerId != null) {
            performer = userService.getUser(performerId).orElse(null);
        }

        Log log = new Log(
            null,
            null,
            null,
            performer,
            description,
            action,
            entityType,
            entityId
        );

        Log createdLog = logRepository.save(log);

        User logPerformer = createdLog.getPerformer();

        LogDto logDto = new LogDto(
            createdLog.getId(),
            createdLog.getCreatedAt(),
            createdLog.getUpdatedAt(),
            extractPerformerId(logPerformer),
            extractPerformerName(logPerformer),
            createdLog.getDescription(),
            createdLog.getAction(),
            createdLog.getEntityType(),
            createdLog.getEntityId()
        );

        logger.debug("Created log: {}", log.getId());

        return logDto;
    }

    public List<LogDto> getLogs(Pageable pageable) {
        return logRepository
            .findAll(pageable)
            .getContent()
            .stream()
            .map(log -> new LogDto(
                log.getId(),
                log.getCreatedAt(),
                log.getUpdatedAt(),
                extractPerformerId(log.getPerformer()),
                extractPerformerName(log.getPerformer()),
                log.getDescription(),
                log.getAction(),
                log.getEntityType(),
                log.getEntityId()
            ))
            .toList()
            .reversed();
    }

    private Long extractPerformerId(User performer) {
        return performer == null ? null : performer.getId();
    }

    private String extractPerformerName(User performer) {
        return performer == null ? "System" : performer.getFirstName() + " " + performer.getLastName();
    }

}
