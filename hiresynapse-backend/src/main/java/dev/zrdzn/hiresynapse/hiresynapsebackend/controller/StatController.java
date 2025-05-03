package dev.zrdzn.hiresynapse.hiresynapsebackend.controller;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.StatisticsDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.CandidateService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.InterviewService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.JobService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.ReportService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/stats")
public class StatController {

    private final CandidateService candidateService;
    private final UserService userService;
    private final ReportService reportService;
    private final JobService jobService;
    private final InterviewService interviewService;

    public StatController(
        CandidateService candidateService,
        UserService userService,
        ReportService reportService,
        JobService jobService,
        InterviewService interviewService
    ) {
        this.candidateService = candidateService;
        this.userService = userService;
        this.reportService = reportService;
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
            jobService.getJobsFromLastSixMonths(),
            interviewService.getInterviewsFromLastSixMonths(),
            userService.getUsersFromLastSixMonths()
        );
    }

}
