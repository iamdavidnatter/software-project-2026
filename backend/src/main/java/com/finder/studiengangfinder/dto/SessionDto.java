package com.finder.studiengangfinder.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SessionDto(
        UUID sessionId,
        UUID resultToken,
        String nickname,
        Integer availableHoursPerWeek,
        OffsetDateTime createdAt
) {
}
