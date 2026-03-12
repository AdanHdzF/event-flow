package com.edacourse.api.subscriber;

import com.edacourse.api.domain.event.OrderCancelledEvent;
import com.edacourse.api.domain.event.OrderCreatedEvent;
import com.edacourse.api.infrastructure.messaging.EventBus;
import com.edacourse.api.infrastructure.messaging.EventSerializer;
import com.edacourse.api.resource.OrderSseResource;

public class SseBridgeSubscriber {
	private final OrderSseResource sseResource;
	private final EventSerializer serializer;

	public SseBridgeSubscriber(EventBus eventBus, EventSerializer serializer, OrderSseResource sseResource) {
		this.sseResource = sseResource;
		this.serializer = serializer;
		eventBus.subscribe("orders.created", OrderCreatedEvent.class, this::onOrderCreated);
		eventBus.subscribe("orders.canceled", OrderCancelledEvent.class, this::onOrderCancelled);
	}

	private void onOrderCreated(OrderCreatedEvent event) {
		sseResource.broadcast("order.created", serializer.serialize(event));
	}

	private void onOrderCancelled(OrderCancelledEvent event) {
		sseResource.broadcast("order.canceled", serializer.serialize(event));
	}
}