package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.StatisticsDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.candidate.CandidateStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.InterviewStatus;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.CandidateService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.InterviewService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.JobService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stats")
public class StatisticController {

    private final CandidateService candidateService;
    private final UserService userService;
    private final JobService jobService;
    private final InterviewService interviewService;

    public StatisticController(
        CandidateService candidateService,
        UserService userService,
        JobService jobService,
        InterviewService interviewService
    ) {
        this.candidateService = candidateService;
        this.userService = userService;
        this.jobService = jobService;
        this.interviewService = interviewService;
    }

    @GetMapping
    public StatisticsDto getStatistics() {
        return new StatisticsDto(
            jobService.getJobCount(),
            candidateService.getCandidateCount(),
            interviewService.getInterviewCount(),
            userService.getUserCount(),
            candidateService.getUtmSourceCount(),
            candidateService.getJobTitleCount(),
            interviewService.getInterviewTypeCount(),
            interviewService.getInterviewStatusCount(),
            candidateService.getCandidatesFromLastSixMonths(),
            candidateService.getCandidatesFromLastSixMonths(CandidateStatus.ACCEPTED),
            candidateService.getCandidatesFromLastSixMonths(CandidateStatus.REJECTED),
            candidateService.getCandidatesFromLastSixMonths(CandidateStatus.PENDING),
            jobService.getJobsFromLastSixMonths(),
            interviewService.getInterviewsFromLastSixMonths(),
            interviewService.getInterviewsFromLastSixMonths(InterviewStatus.SCHEDULED),
            interviewService.getInterviewsFromLastSixMonths(InterviewStatus.COMPLETED),
            userService.getUsersFromLastSixMonths()
        );
    }

}
