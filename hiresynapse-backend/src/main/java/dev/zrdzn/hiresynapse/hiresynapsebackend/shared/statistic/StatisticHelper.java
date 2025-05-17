package dev.zrdzn.hiresynapse.hiresynapsebackend.shared.statistic;

import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.MonthlyDataDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatisticHelper {

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM");

    public static <T extends StatisticPoint> MonthlyDataDto getMonthlyData(List<T> list) {
        Map<String, Integer> monthlyData = countByMonth(list);
        double growthRate = calculateGrowthRate(monthlyData);

        return new MonthlyDataDto(
            list.size(),
            growthRate,
            monthlyData
        );
    }

    public static <T extends StatisticPoint> Map<String, Integer> countByMonth(List<T> list) {
        Map<String, Integer> months = fillLastSixMonths();

        // count the number of items in each month
        for (T item : list) {
            Instant createdAt = item.getCreatedAt();
            if (createdAt != null) {
                LocalDate date = LocalDate.ofInstant(createdAt, ZoneId.systemDefault());
                String month = date.format(MONTH_FORMATTER);

                months.put(month, months.get(month) + 1);
            }
        }

        return months;
    }

    public static double calculateGrowthRate(Map<String, Integer> monthlyData) {
        // need at least two months of data to calculate growth rate
        if (monthlyData.size() < 2) {
            return 0.0;
        }

        List<String> months = List.copyOf(monthlyData.keySet());

        String currentMonth = months.getLast();
        String previousMonth = months.get(months.size() - 2);

        int currentCount = monthlyData.get(currentMonth);
        int previousCount = monthlyData.get(previousMonth);

        // if previous count was 0, and the current count is greater than 0, it will always be 100% growth
        if (previousCount == 0) {
            return currentCount > 0 ? 100.0 : 0.0;
        }

        int difference = currentCount - previousCount;

        return (double) (difference / previousCount) * 100;
    }

    private static Map<String, Integer> fillLastSixMonths() {
        // linked hashmap to keep the order of months
        Map<String, Integer> months = new LinkedHashMap<>();

        LocalDate now = LocalDate.now();

        // fill last six months with 0s
        for (int monthIndex = 5; monthIndex >= 0; monthIndex--) {
            LocalDate date = now.minusMonths(monthIndex);

            months.put(date.format(MONTH_FORMATTER), 0);
        }

        return months;
    }

}
