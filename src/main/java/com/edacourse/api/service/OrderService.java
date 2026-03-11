package com.edacourse.api.service;

// import com.edacourse.api.PromotionService;
import com.edacourse.api.domain.Order;
import com.edacourse.api.dto.CreateOrderRequest;
import com.edacourse.api.infrastructure.messaging.EventBus;
import com.edacourse.api.infrastructure.messaging.OrderEvent;
import com.edacourse.api.repository.OrderRepository;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class OrderService {
	// private final NotificationService notificationService;
	// private final PromotionService promotionService;
	private final EventBus eventBus;
	private final OrderRepository orderRepository;

	// public OrderService(NotificationService notificationService, PromotionService
	// promotionService, EventBus eventBus) {
	@Inject
	public OrderService(EventBus eventBus, OrderRepository orderRepository) {
		// this.notificationService = notificationService;
		// this.promotionService = promotionService;
		this.eventBus = eventBus;
		this.orderRepository = orderRepository;
	}

	public Order createOrder(CreateOrderRequest dto) {
		String orderDetails = "Producto: " + dto.getProduct() + ", Precio: " + dto.getPrice();
		System.out.println("Orden creada: " + orderDetails);

		// promotionService.applyPromotion(dto.getPrice());
		// notificationService.notify("Nueva orden creada: " + orderDetails);

		Order order = new Order(dto.getProduct(), dto.getPrice(), dto.getQuantity());
		orderRepository.save(order);

		eventBus.publish("order.created", new OrderEvent(order.getProduct(), order.getPrice()));

		return order;
	}

}