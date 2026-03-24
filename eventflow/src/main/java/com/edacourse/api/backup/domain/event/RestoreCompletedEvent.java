package com.edacourse.api.backup.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record RestoreCompletedEvent(
		String restoreId,
		String snapshotId,
		int restoredFiles,
		long durationMs,
		Instant completedAt) implements DomainEvent {
}
