package com.edacourse.api.notification.domain.repository;

import java.util.List;
import java.util.Optional;

import com.edacourse.api.notification.domain.model.NotificationWebhook;

public interface NotificationRepository {
	void save(NotificationWebhook notification);

	Optional<NotificationWebhook> findByChannelName(String channelName);

	List<NotificationWebhook> findAll();

}
