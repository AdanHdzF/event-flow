package com.edacourse.pubsub.channel.model;

import java.time.Instant;

public class Channel {
	private String id;
	private String name;
	private String description;
	private Instant createdAt;

	public Channel() {
	}

	public Channel(String id, String name, String description, Instant createdAt) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
