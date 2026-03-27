package com.edacourse.api.saga.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

/**
 * Evento de compensacion: inventario liberado tras fallo en el checkout.
 */
public record InventoryReleasedEvent(
		String sagaId,
		String orderId,
		Instant releasedAt) implements DomainEvent {
}
