package com.finder.studiengangfinder.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FavoriteUpdateRequest(
        @NotNull List<Long> programIds
) {
}
