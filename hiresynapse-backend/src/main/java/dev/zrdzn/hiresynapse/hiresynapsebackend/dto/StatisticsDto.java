package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

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
    MonthlyDataDto jobsFromLastSixMonths,
    MonthlyDataDto interviewsFromLastSixMonths,
    MonthlyDataDto usersFromLastSixMonths
) {
}
