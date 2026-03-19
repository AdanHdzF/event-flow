package com.edacourse.pubsub.channel.model;

import java.time.Instant;

public class Message {
	private String id;
	private String channelId;
	private String payload;
	private String publisherId;
	private Instant publishedAt;

	public Message() {
	}

	public Message(String id, String channelId, String payload, String publisherId, Instant publishedAt) {
		this.id = id;
		this.channelId = channelId;
		this.payload = payload;
		this.publisherId = publisherId;
		this.publishedAt = publishedAt;
	}

	public String getId() {
		return id;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getPayload() {
		return payload;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public Instant getPublishedAt() {
		return publishedAt;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public void setPublishedAt(Instant publishedAt) {
		this.publishedAt = publishedAt;
	}

}
