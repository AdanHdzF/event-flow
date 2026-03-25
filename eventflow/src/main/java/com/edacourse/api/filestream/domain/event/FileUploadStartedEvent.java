package com.edacourse.api.filestream.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

public record FileUploadStartedEvent(
		String fileId,
		String fileName,
		int totalParts,
		long totalBytes,
		Instant startedAt) implements DomainEvent {
}
