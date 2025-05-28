package dev.zrdzn.hiresynapse.hiresynapsebackend.statistic;

import dev.zrdzn.hiresynapse.hiresynapsebackend.ApplicationRunner;
import dev.zrdzn.hiresynapse.hiresynapsebackend.dto.statistic.StatisticsDto;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StatisticControllerTest extends ApplicationRunner {

    @Test
    void testGetStatistics() {
        authenticateAs(defaultUserId);

        HttpResponse<StatisticsDto> response = Unirest.get("/stats")
            .asObject(StatisticsDto.class);

        assertEquals(200, response.getStatus());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().jobCount());
        assertNotNull(response.getBody().candidateCount());
        assertNotNull(response.getBody().interviewCount());
        assertNotNull(response.getBody().userCount());
        assertNotNull(response.getBody().utmSourceCount());
        assertNotNull(response.getBody().jobTitleCount());
        assertNotNull(response.getBody().interviewTypeCount());
        assertNotNull(response.getBody().interviewStatusCount());
        assertNotNull(response.getBody().candidatesFromLastSixMonths());
        assertNotNull(response.getBody().acceptedCandidatesFromLastSixMonths());
        assertNotNull(response.getBody().rejectedCandidatesFromLastSixMonths());
        assertNotNull(response.getBody().pendingCandidatesFromLastSixMonths());
        assertNotNull(response.getBody().jobsFromLastSixMonths());
        assertNotNull(response.getBody().interviewsFromLastSixMonths());
        assertNotNull(response.getBody().scheduledInterviewsFromLastSixMonths());
        assertNotNull(response.getBody().completedInterviewsFromLastSixMonths());
        assertNotNull(response.getBody().usersFromLastSixMonths());
    }
} 