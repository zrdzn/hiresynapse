package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.Log;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogAction;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.LogEntityType;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.repository.LogRepository;
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

    public Log createLog(Long performerId, String description, LogAction action, LogEntityType entityType, long entityId) {
        User performer = userService.getUser(performerId).orElse(null);

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

        logger.debug("Created log: {}", log.getId());

        return createdLog;
    }

    public List<Log> getLogs(Pageable pageable) {
        return logRepository.findAll(pageable).getContent();
    }

}
