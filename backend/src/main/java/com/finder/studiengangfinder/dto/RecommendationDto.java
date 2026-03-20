package com.finder.studiengangfinder.dto;

import java.util.List;

public record RecommendationDto(
        Long programId,
        String programName,
        Integer score,
        String rationale,
        String dynamicRiskSummary,
        Integer estimatedWorkloadHoursPerWeek,
        Integer availableHoursPerWeek,
        boolean favorite,
        StudyProgramDto details,
        List<String> highlights
) {
}
