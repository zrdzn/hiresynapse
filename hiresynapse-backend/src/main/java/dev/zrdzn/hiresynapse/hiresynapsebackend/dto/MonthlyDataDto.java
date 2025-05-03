package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import java.util.Map;

public record MonthlyDataDto(
    int total,
    double growthRate,
    Map<String, Integer> monthlyData
) {
}
