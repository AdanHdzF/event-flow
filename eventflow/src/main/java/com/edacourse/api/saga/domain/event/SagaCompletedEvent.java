package com.edacourse.api.saga.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record SagaCompletedEvent(
		String sagaId,
		String orderId,
		long durationMs,
		Instant completedAt) implements DomainEvent {
}
