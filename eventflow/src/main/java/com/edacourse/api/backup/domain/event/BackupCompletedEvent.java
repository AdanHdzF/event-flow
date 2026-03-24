package com.edacourse.api.backup.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record BackupCompletedEvent(
		String backupId,
		String snapshotId,
		long sizeBytes,
		long durationMs,
		Instant completedAt) implements DomainEvent {
}
