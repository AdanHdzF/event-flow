package com.edacourse.api.notification.domain.model;

public class NotificationWebhook {
	private final String channelName;
	private final String secret;

	public NotificationWebhook(String channelName, String secret) {
		this.channelName = channelName;
		this.secret = secret;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getSecret() {
		return secret;
	}

}
