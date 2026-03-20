package com.finder.studiengangfinder.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record AnswerSubmissionItemDto(
        @NotBlank String questionKey,
        List<String> selectedValues,
        Integer scaleValue
) {
}
