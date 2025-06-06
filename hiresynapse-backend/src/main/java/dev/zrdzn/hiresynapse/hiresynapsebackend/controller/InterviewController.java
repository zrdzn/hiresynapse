package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.interview.InterviewCreateDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.Interview;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.UserPrincipal;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.InterviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<Interview> createInterview(
        @AuthenticationPrincipal UserPrincipal principal,
        @RequestBody InterviewCreateDto interviewCreateDto
    ) {
        return ResponseEntity.ok(interviewService.initiateInterviewCreation(principal.getUser().getId(), interviewCreateDto));
    }

    @GetMapping
    public List<Interview> getInterviews(
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return interviewService.getInterviews(pageable);
    }

    @GetMapping("/confirmed")
    public List<Interview> getConfirmedInterviews(
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return interviewService.getConfirmedInterviews(pageable);
    }

    @GetMapping("/unconfirmed")
    public List<Interview> getUnconfirmedInterviews(
        @PageableDefault(size = 50) Pageable pageable
    ) {
        return interviewService.getUnconfirmedInterviews(pageable);
    }

    @GetMapping("/{interviewId}")
    public ResponseEntity<Interview> getInterview(@PathVariable long interviewId) {
        Optional<Interview> interview = interviewService.getInterview(interviewId);

        return interview.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{interviewId}/complete")
    public void completeInterview(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long interviewId
    ) {
        interviewService.completeInterview(principal.getUser().getId(), interviewId);
    }

    @PatchMapping("/{interviewId}/cancel")
    public void cancelInterview(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long interviewId
    ) {
        interviewService.cancelInterview(principal.getUser().getId(), interviewId);
    }

    @DeleteMapping("/{interviewId}")
    public void deleteInterview(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable long interviewId
    ) {
        interviewService.deleteInterview(principal.getUser().getId(), interviewId);
    }

}
