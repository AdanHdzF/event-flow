package com.edacourse.api.saga.domain.event;

import java.time.Instant;
import java.util.List;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record SagaFailedEvent(
		String sagaId,
		String orderId,
		String failedStep,
		String reason,
		List<String> compensatedSteps,
		Instant failedAt) implements DomainEvent {
}
