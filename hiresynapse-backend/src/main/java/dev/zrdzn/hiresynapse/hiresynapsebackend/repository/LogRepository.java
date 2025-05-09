package dev.zrdzn.hiresynapse.hiresynapsebackend.repository;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
