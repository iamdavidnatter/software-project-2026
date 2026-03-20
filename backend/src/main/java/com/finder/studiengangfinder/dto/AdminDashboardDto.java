package com.finder.studiengangfinder.dto;

import java.util.List;

public record AdminDashboardDto(
        long totalSessions,
        long completedResults,
        long totalFavorites,
        String mostPopularStudyModel,
        List<ProgramInterestSummaryDto> topPrograms,
        List<RecentResultDto> recentResults
) {
}
