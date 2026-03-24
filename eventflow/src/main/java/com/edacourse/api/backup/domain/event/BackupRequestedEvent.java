package com.edacourse.api.backup.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record BackupRequestedEvent(
		String backupId,
		String description,
		Instant requestedAt) implements DomainEvent {
}
