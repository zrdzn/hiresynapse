package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.CandidateCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserPrincipal;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.CandidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/candidates")
public class CandidateController {

    private final Logger logger = LoggerFactory.getLogger(CandidateController.class);

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public ResponseEntity<Candidate> createCandidate(
        @RequestPart("dto") CandidateCreateDto candidateCreateDto,
        @RequestPart("file") MultipartFile resumeFile
        ) {
        return ResponseEntity.ok(candidateService.initiateCandidateCreation(candidateCreateDto, resumeFile.getOriginalFilename(), resumeFile));
    }

    @GetMapping
    public List<Candidate> getCandidates(
        @AuthenticationPrincipal UserPrincipal principal,
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return candidateService.getCandidates(pageable);
    }

    @GetMapping("/{candidateId}")
    public ResponseEntity<Candidate> getCandidate(@PathVariable String candidateId) {
        Optional<Candidate> candidate = candidateService.getCandidate(candidateId);

        return candidate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
