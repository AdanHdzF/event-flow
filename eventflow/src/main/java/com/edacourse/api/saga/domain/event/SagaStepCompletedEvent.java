package com.edacourse.api.saga.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record SagaStepCompletedEvent(
		String sagaId,
		String orderId,
		String step,
		String status,
		Instant completedAt) implements DomainEvent {
}
