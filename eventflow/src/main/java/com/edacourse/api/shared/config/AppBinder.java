package com.edacourse.api.shared.config;

import org.glassfish.jersey.internal.inject.AbstractBinder;

import com.edacourse.api.backup.application.BackupService;
import com.edacourse.api.backup.application.DataSeeder;
import com.edacourse.api.catalog.application.service.CatalogService;
import com.edacourse.api.notification.application.service.NotificationService;
import com.edacourse.api.notification.domain.repository.NotificationRepository;
import com.edacourse.api.notification.infrastructure.persistence.InMemoryNotificationRepository;
import com.edacourse.api.order.application.service.OrderService;
import com.edacourse.api.order.domain.repository.OrderRepository;
import com.edacourse.api.order.infrastructure.persistence.InMemoryOrderRepository;
import com.edacourse.api.order.interfaces.sse.OrderSseResource;
import com.edacourse.api.search.application.service.SearchService;
import com.edacourse.api.security.HmacSigner;
import com.edacourse.api.shared.infrastructure.messaging.EventBus;
import com.edacourse.api.shared.infrastructure.serialization.EventSerializer;
import com.edacourse.api.shared.infrastructure.sse.EventSseBroadcaster;

import jakarta.inject.Singleton;

public class AppBinder extends AbstractBinder {
	private final EventSerializer serializer;
	private final EventBus eventBus;
	private final OrderSseResource sseResource;
	private final CatalogService catalogService;
	private final SearchService searchService;
	private final HmacSigner hmacSigner;
	private final EventSseBroadcaster sseBroadcaster;
	private final BackupService backupService;

	public AppBinder(EventSerializer serializer, EventBus eventBus, OrderSseResource sseResource,
			CatalogService catalogService, SearchService searchService, HmacSigner hmacSigner,
			EventSseBroadcaster sseBroadcaster, BackupService backupService) {
		this.serializer = serializer;
		this.eventBus = eventBus;
		this.sseResource = sseResource;
		this.catalogService = catalogService;
		this.searchService = searchService;
		this.hmacSigner = hmacSigner;
		this.sseBroadcaster = sseBroadcaster;
		this.backupService = backupService;
	}

	@Override
	protected void configure() {
		bind(serializer).to(EventSerializer.class);
		bind(eventBus).to(EventBus.class);

		bind(InMemoryOrderRepository.class).to(OrderRepository.class).in(Singleton.class);
		bind(OrderService.class).to(OrderService.class).in(Singleton.class);
		bind(sseResource).to(OrderSseResource.class).in(Singleton.class);
		bind(catalogService).to(CatalogService.class);
		bind(searchService).to(SearchService.class);

		bind(InMemoryNotificationRepository.class).to(NotificationRepository.class).in(Singleton.class);
		bind(NotificationService.class).to(NotificationService.class).in(Singleton.class);

		bind(hmacSigner).to(HmacSigner.class);

		bind(sseBroadcaster).to(EventSseBroadcaster.class);

		bind(backupService).to(BackupService.class).in(Singleton.class);
		bind(DataSeeder.class).to(DataSeeder.class).in(Singleton.class);
	}
}
