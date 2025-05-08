package dev.zrdzn.hiresynapse.hiresynapsebackend.repository;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.InterviewStatusCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.InterviewTypeCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Interview;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    Page<Interview> findAllByTaskStatus(TaskStatus taskStatus, Pageable pageable);

    Page<Interview> findByStatusAndTaskStatusOrderByInterviewAtAsc(InterviewStatus interviewStatus, TaskStatus taskStatus, Pageable pageable);

    @Query(
        """
        SELECT new dev.zrdzn.hiresynapse.hiresynapsebackend.dto.InterviewStatusCountDto(interview.status, COUNT(interview), null)
        FROM Interview interview
        GROUP BY interview.status
        ORDER BY COUNT(interview) DESC
        """)
    List<InterviewStatusCountDto> findInterviewStatusCounts();

    @Query(
        """
        SELECT new dev.zrdzn.hiresynapse.hiresynapsebackend.dto.InterviewTypeCountDto(interview.interviewType, COUNT(interview), null)
        FROM Interview interview
        GROUP BY interview.interviewType
        ORDER BY COUNT(interview) DESC
        """)
    List<InterviewTypeCountDto> findInterviewTypeCounts();

    @Query(
        """
        SELECT interview
        FROM Interview interview
        WHERE interview.createdAt >= :startDate
        """
    )
    List<Interview> findInterviewsCreatedAfter(@Param("startDate") Instant startDate);

}
