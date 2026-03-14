package com.edacourse.api.config;

import org.glassfish.jersey.internal.inject.AbstractBinder;

import com.edacourse.api.infrastructure.messaging.EventSerializer;
import com.edacourse.api.infrastructure.messaging.RoutablePublisher;
import com.edacourse.api.repository.InMemoryOrderRepository;
import com.edacourse.api.repository.OrderRepository;
import com.edacourse.api.resource.OrderSseResource;
import com.edacourse.api.service.OrderService;

import jakarta.inject.Singleton;

public class AppBinder extends AbstractBinder {
	private final EventSerializer serializer;
	private final RoutablePublisher eventBus;
	private final OrderSseResource orderSseResource;

	public AppBinder(EventSerializer serializer, RoutablePublisher eventBus, OrderSseResource orderSseResource) {
		this.serializer = serializer;
		this.eventBus = eventBus;
		this.orderSseResource = orderSseResource;
	}

	@Override
	protected void configure() {
		bind(serializer).to(EventSerializer.class);
		bind(eventBus).to(RoutablePublisher.class);
		bind(orderSseResource).to(OrderSseResource.class).in(Singleton.class);

		bind(InMemoryOrderRepository.class).to(OrderRepository.class).in(Singleton.class);
		bind(OrderService.class).to(OrderService.class).in(Singleton.class);

	}

}
