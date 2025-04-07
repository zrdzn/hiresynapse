package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.Candidate;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserPrincipal;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.CandidateService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public Candidate createCandidate(@RequestBody Candidate candidate) {
        return candidateService.initiateCandidateCreation(candidate);
    }

    @GetMapping
    public List<Candidate> getCandidates(
        @AuthenticationPrincipal UserPrincipal principal,
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return candidateService.getCandidates(principal.getUser().id(), pageable);
    }

}
