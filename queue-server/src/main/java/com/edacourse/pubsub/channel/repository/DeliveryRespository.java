package com.edacourse.pubsub.channel.repository;

import java.util.List;

import com.edacourse.pubsub.channel.model.DeliveryRecord;

public interface DeliveryRespository {
	void save(DeliveryRecord record);

	List<DeliveryRecord> findByChannelName(String channelName, int limit);

	List<DeliveryRecord> findBySubscriptionId(String subscriptionId, int limit);
}
