package com.finder.studiengangfinder.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CompareProgramsRequest(
        @NotEmpty List<Long> programIds
) {
}
