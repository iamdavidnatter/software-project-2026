package com.finder.studiengangfinder.dto;

public record ProgramInterestSummaryDto(
        Long programId,
        String programName,
        long topRecommendationCount,
        double averageScore,
        long favoriteCount
) {
}
