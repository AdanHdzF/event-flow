package com.edacourse.api.filestream.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record CatalogImportFailedEvent(
		String fileId,
		String error,
		Instant failedAt) implements DomainEvent {
}
