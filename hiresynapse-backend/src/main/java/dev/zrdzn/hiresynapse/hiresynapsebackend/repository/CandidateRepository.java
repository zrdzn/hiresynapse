package dev.zrdzn.hiresynapse.hiresynapsebackend.repository;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.JobTitleCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.UtmSourceCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.CandidateStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.TaskStatus;
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
        SELECT new dev.zrdzn.hiresynapse.hiresynapsebackend.dto.JobTitleCountDto(candidate.job.title, COUNT(candidate), null)
        FROM Candidate candidate
        GROUP BY candidate.job.title
        ORDER BY COUNT(candidate) DESC
        """)
    List<JobTitleCountDto> findJobTitleCounts();

    @Query(
        """
        SELECT new dev.zrdzn.hiresynapse.hiresynapsebackend.dto.UtmSourceCountDto(candidate.utmSource, COUNT(candidate), null)
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

    void deleteByJobId(long jobId);

}
