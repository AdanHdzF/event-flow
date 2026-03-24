package com.edacourse.api.backup.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record BackupFailedEvent(
		String backupId,
		String error,
		Instant failedAt) implements DomainEvent {
}
