package com.finder.studiengangfinder.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ResultDto(
        UUID token,
        String nickname,
        OffsetDateTime generatedAt,
        Integer availableHoursPerWeek,
        String profileSummary,
        List<String> selectedInterests,
        List<String> selectedSkills,
        List<String> selectedPreferences,
        List<Long> favoriteProgramIds,
        RecommendationDto topRecommendation,
        List<RecommendationDto> recommendations
) {
}
