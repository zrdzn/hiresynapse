package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import java.util.List;

public record StatisticsDto(
    int jobCount,
    int candidateCount,
    int interviewCount,
    int userCount,
    List<JobTitleCountDto> jobTitleCount,
    List<InterviewTypeCountDto> interviewTypeCount,
    MonthlyDataDto candidatesFromLastSixMonths,
    MonthlyDataDto jobsFromLastSixMonths,
    MonthlyDataDto interviewsFromLastSixMonths,
    MonthlyDataDto usersFromLastSixMonths
) {
}
