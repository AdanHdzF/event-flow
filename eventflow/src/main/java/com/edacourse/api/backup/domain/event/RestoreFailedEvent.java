package com.edacourse.api.backup.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record RestoreFailedEvent(
		String restoreId,
		String error,
		Instant failedAt) implements DomainEvent {
}
