package dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.interview.InterviewStatusCountDto;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.interview.InterviewTypeCountDto;

import java.util.List;

public record StatisticsDto(
    int jobCount,
    int candidateCount,
    int interviewCount,
    int userCount,
    List<UtmSourceCountDto> utmSourceCount,
    List<JobTitleCountDto> jobTitleCount,
    List<InterviewTypeCountDto> interviewTypeCount,
    List<InterviewStatusCountDto> interviewStatusCount,
    MonthlyDataDto candidatesFromLastSixMonths,
    MonthlyDataDto acceptedCandidatesFromLastSixMonths,
    MonthlyDataDto rejectedCandidatesFromLastSixMonths,
    MonthlyDataDto pendingCandidatesFromLastSixMonths,
    MonthlyDataDto jobsFromLastSixMonths,
    MonthlyDataDto interviewsFromLastSixMonths,
    MonthlyDataDto scheduledInterviewsFromLastSixMonths,
    MonthlyDataDto completedInterviewsFromLastSixMonths,
    MonthlyDataDto usersFromLastSixMonths
) {
}
