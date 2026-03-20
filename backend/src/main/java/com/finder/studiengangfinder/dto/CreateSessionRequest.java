package com.finder.studiengangfinder.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record CreateSessionRequest(
        @Size(max = 120) String nickname,
        @Min(0) @Max(80) Integer availableHoursPerWeek
) {
}
