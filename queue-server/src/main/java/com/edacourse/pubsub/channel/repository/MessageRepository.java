package com.edacourse.pubsub.channel.repository;

import java.util.List;

import com.edacourse.pubsub.channel.model.Message;

public interface MessageRepository {
	Message save(Message message);

	List<Message> findByChannelId(String channelId, int limit);
}
