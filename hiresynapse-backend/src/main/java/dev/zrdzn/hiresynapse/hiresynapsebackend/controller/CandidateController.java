package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.CandidateCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.UserPrincipal;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.CandidateService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return candidateService.getCandidates(pageable);
    }

    @GetMapping("/pending")
    public List<Candidate> getPendingCandidates(
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return candidateService.getPendingCandidates(pageable);
    }

    @GetMapping("/{candidateId}")
    public ResponseEntity<Candidate> getCandidate(@PathVariable long candidateId) {
        Optional<Candidate> candidate = candidateService.getCandidate(candidateId);

        return candidate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{candidateId}/accept")
    public void acceptCandidate(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long candidateId
    ) {
        candidateService.acceptCandidate(principal.getUser().getId(), candidateId);
    }

    @PatchMapping("/{candidateId}/reject")
    public void rejectCandidate(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long candidateId
    ) {
        candidateService.rejectCandidate(principal.getUser().getId(), candidateId);
    }

}
