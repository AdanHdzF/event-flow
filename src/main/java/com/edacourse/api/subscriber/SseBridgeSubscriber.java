package com.edacourse.api.subscriber;

import com.edacourse.api.domain.event.OrderCancelledEvent;
import com.edacourse.api.domain.event.OrderCreatedEvent;
import com.edacourse.api.infrastructure.messaging.EventSerializer;
import com.edacourse.api.infrastructure.messaging.RoutableSubscriber;
import com.edacourse.api.resource.OrderSseResource;

public class SseBridgeSubscriber {
	// private final String CONSUMER_GROUP = "sse-bridge-group";
	private final OrderSseResource sseResource;
	private final EventSerializer serializer;

	public SseBridgeSubscriber(RoutableSubscriber eventBus, EventSerializer serializer, OrderSseResource sseResource) {
		this.sseResource = sseResource;
		this.serializer = serializer;
		eventBus.subscribe("orders.created", "orders.created", OrderCreatedEvent.class, this::onOrderCreated);
		eventBus.subscribe("orders.canceled", "orders.canceled", OrderCancelledEvent.class, this::onOrderCancelled);
	}

	private void onOrderCreated(OrderCreatedEvent event) {
		sseResource.broadcast("order.created", serializer.serialize(event));
	}

	private void onOrderCancelled(OrderCancelledEvent event) {
		sseResource.broadcast("order.canceled", serializer.serialize(event));
	}
}