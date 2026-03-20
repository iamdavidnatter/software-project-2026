package com.finder.studiengangfinder.dto;

import java.util.List;

public record StudyProgramDto(
        Long id,
        String name,
        String shortDescription,
        String studyModel,
        Integer durationMonths,
        Integer estimatedWorkloadHoursPerWeek,
        Integer theoryLevel,
        Integer flexibilityLevel,
        List<String> interestTags,
        List<String> skillTags,
        List<String> preferenceTags,
        List<SimpleListItemDto> contents,
        List<SimpleListItemDto> careerPaths,
        List<SimpleListItemDto> risks,
        List<String> applicationChecklist,
        List<DailyRoutineEntryDto> dailyRoutine,
        InternationalSupportInfoDto internationalSupport
) {
}
