package dev.zrdzn.hiresynapse.hiresynapsebackend.repository;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.JobTitleCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.UtmSourceCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.CandidateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    Page<Candidate> findAllByTaskStatus(TaskStatus taskStatus, Pageable pageable);

    Page<Candidate> findAllByTaskStatusAndStatus(TaskStatus taskStatus, CandidateStatus status, Pageable pageable);

    @Query(
        """
        SELECT new dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.JobTitleCountDto(candidate.job.title, COUNT(candidate), null)
        FROM Candidate candidate
        GROUP BY candidate.job.title
        ORDER BY COUNT(candidate) DESC
        """)
    List<JobTitleCountDto> findJobTitleCounts();

    @Query(
        """
        SELECT new dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.UtmSourceCountDto(candidate.utmSource, COUNT(candidate), null)
        FROM Candidate candidate
        GROUP BY candidate.utmSource
        ORDER BY COUNT(candidate) DESC
        """
    )
    List<UtmSourceCountDto> findUtmSourceCounts();

    @Query(
        """
        SELECT candidate
        FROM Candidate candidate
        WHERE candidate.createdAt >= :startDate
        """
    )
    List<Candidate> findCandidatesCreatedAfter(@Param("startDate") Instant startDate);

    @Query(
        """
        SELECT candidate
        FROM Candidate candidate
        WHERE candidate.createdAt >= :startDate
        AND candidate.status = :status
        """
    )
    List<Candidate> findCandidatesCreatedAfter(
        @Param("startDate") Instant startDate,
        @Param("status") CandidateStatus status
    );

    void deleteByJobId(long jobId);

}
