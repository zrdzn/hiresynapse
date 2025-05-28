package dev.zrdzn.hiresynapse.hiresynapsebackend.candidate;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.CandidateCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.job.JobSpec;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.CandidateService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.mock.web.MockMultipartFile;

public class CandidateSpec extends JobSpec {

    protected final CandidateService candidateService = context.getBean(CandidateService.class);

    protected Candidate candidate;
    protected MockMultipartFile resumeFile;

    @BeforeEach
    public void setupCandidates() {
        resumeFile = new MockMultipartFile(
            "resume.pdf",
            "resume.pdf",
            "application/pdf",
            "test resume content".getBytes()
        );

        candidate = candidateService.initiateCandidateCreation(
            new CandidateCreateDto(
                "test@example.com",
                job.getId(),
                "direct"
            ),
            resumeFile.getOriginalFilename(),
            resumeFile
        );
    }
} 