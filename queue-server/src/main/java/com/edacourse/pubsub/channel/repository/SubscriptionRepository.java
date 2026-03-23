package com.edacourse.pubsub.channel.repository;

import java.util.List;
import java.util.Optional;

import com.edacourse.pubsub.channel.model.Subscription;

public interface SubscriptionRepository {
	Subscription save(Subscription subscription);

	Optional<Subscription> findById(String id);

	List<Subscription> findByChannelId(String channelId);

	List<Subscription> findActiveByChannelId(String channelId);

	List<Subscription> findActiveByChannelName(String channelName);

	List<Subscription> findActiveByChannelIdWebhook(String channelName, String webhookUrl);

	void deactivate(String id);
}
