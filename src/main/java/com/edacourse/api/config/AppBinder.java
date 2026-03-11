package com.edacourse.api.config;

import org.glassfish.jersey.internal.inject.AbstractBinder;

import com.edacourse.api.infrastructure.messaging.EventBus;
import com.edacourse.api.infrastructure.messaging.EventSerializer;
import com.edacourse.api.repository.InMemoryOrderRepository;
import com.edacourse.api.repository.OrderRepository;
import com.edacourse.api.service.OrderService;

import jakarta.inject.Singleton;

public class AppBinder extends AbstractBinder {

	private final EventSerializer eventSerializer;
	private final EventBus eventBus;

	public AppBinder(EventSerializer eventSerializer, EventBus eventBus) {
		this.eventSerializer = eventSerializer;
		this.eventBus = eventBus;
	}

	@Override
	protected void configure() {
		bind(eventSerializer).to(EventSerializer.class);
		bind(eventBus).to(EventBus.class);

		bind(InMemoryOrderRepository.class).to(OrderRepository.class).in(Singleton.class);
		bind(OrderService.class).to(OrderService.class).in(Singleton.class);
	}
}
