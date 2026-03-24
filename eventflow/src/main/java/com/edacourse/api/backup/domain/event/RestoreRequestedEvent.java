package com.edacourse.api.backup.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record RestoreRequestedEvent(
		String restoreId,
		String snapshotId,
		Instant requestedAt) implements DomainEvent {
}
