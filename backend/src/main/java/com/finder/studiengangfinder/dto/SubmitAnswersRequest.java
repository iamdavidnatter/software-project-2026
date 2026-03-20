package com.finder.studiengangfinder.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SubmitAnswersRequest(
        @Valid @NotEmpty List<AnswerSubmissionItemDto> answers
) {
}
