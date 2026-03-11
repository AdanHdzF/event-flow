package com.edacourse.api.infrastructure.messaging;

public class InventorySubscriber {
	public InventorySubscriber(EventBus eventBus) {

		eventBus.subscribe("order.created", OrderEvent.class, this::handleOrderCreated);
	}

	private void handleOrderCreated(OrderEvent event) {
		System.out.println(
				"InventorySubscriber received [order.created] event: " + event.product() + " at $" + event.price());
		// Aquí podrías implementar lógica para actualizar el inventario, etc.
	}
}
