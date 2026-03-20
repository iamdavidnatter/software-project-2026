package com.finder.studiengangfinder.dto;

import java.util.List;

public record QuestionnaireDto(
        String title,
        String subtitle,
        List<QuestionDto> questions
) {
}
