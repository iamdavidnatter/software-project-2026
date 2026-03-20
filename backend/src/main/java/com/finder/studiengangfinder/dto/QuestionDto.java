package com.finder.studiengangfinder.dto;

import java.util.List;

public record QuestionDto(
        Long id,
        String key,
        Integer sortOrder,
        String title,
        String description,
        String type,
        boolean required,
        Integer minScale,
        Integer maxScale,
        List<QuestionOptionDto> options
) {
}
