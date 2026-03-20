package com.finder.studiengangfinder.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RecentResultDto(
        String nickname,
        OffsetDateTime generatedAt,
        UUID token,
        String topProgramName,
        Integer topScore,
        Integer availableHoursPerWeek
) {
}
