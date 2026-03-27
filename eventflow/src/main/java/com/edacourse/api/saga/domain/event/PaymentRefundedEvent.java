package com.edacourse.api.saga.domain.event;

import java.time.Instant;

import com.edacourse.api.shared.domain.event.DomainEvent;

/**
 * Evento de compensacion: pago reembolsado tras fallo en el checkout.
 */
public record PaymentRefundedEvent(
		String sagaId,
		String orderId,
		double amount,
		Instant refundedAt) implements DomainEvent {
}
