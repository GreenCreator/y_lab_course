package io.domain;

import io.domain.enums.HealthStatus;

public record HealthResponseDto(HealthStatus status) {
}
