package com.edacourse.api.filestream.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record CatalogImportCompletedEvent(
		String fileId,
		String fileName,
		int productsImported,
		long durationMs,
		Instant completedAt) implements DomainEvent {
}
