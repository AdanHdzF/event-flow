package com.edacourse.api.notification.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.edacourse.api.notification.domain.model.NotificationWebhook;
import com.edacourse.api.notification.domain.repository.NotificationRepository;

public class InMemoryNotificationRepository implements NotificationRepository {
	private final Map<String, NotificationWebhook> notifications = new ConcurrentHashMap<>();

	@Override
	public void save(NotificationWebhook notification) {
		notifications.put(notification.getChannelName(), notification);
	}

	@Override
	public Optional<NotificationWebhook> findByChannelName(String channelName) {
		return Optional.ofNullable(notifications.get(channelName));
	}

	@Override
	public List<NotificationWebhook> findAll() {
		return new ArrayList<>(notifications.values());
	}

}
