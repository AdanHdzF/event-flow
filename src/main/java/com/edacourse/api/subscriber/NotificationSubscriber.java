package com.edacourse.api.subscriber;

import com.edacourse.api.domain.event.InventoryReservedEvent;
import com.edacourse.api.domain.event.PaymentCompletedEvent;
import com.edacourse.api.infrastructure.messaging.RoutableSubscriber;
import com.edacourse.api.service.NotificationService;

public class NotificationSubscriber {
	// private static final String CONSUMER_GROUP = "notification-service-group";
	private final NotificationService notificationService;

	public NotificationSubscriber(RoutableSubscriber eventBus, NotificationService notificationService) {
		this.notificationService = notificationService;

		eventBus.subscribe("inventory.reserved", "inventory.reserved", InventoryReservedEvent.class,
				this::onInventoryReserved);
		eventBus.subscribe("payment.completed", "payment.completed", PaymentCompletedEvent.class,
				this::onPaymentCompleted);
	}

	private void onInventoryReserved(InventoryReservedEvent event) {
		// System.out.println("[NotificationSubscriber] onInventoryReserved");

		notificationService.notifyEvent("inventory.reserved",
				"********** Inventory reserved for Product: " + event.product());
	}

	private void onPaymentCompleted(PaymentCompletedEvent event) {
		// System.out.println("[NotificationSubscriber] onPaymentCompleted");

		notificationService.notifyEvent("payment.completed",
				"********** Payment completed for Order ID: " + event.orderId());
	}

}
