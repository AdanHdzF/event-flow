package com.edacourse.api.catalog.infrastructure.cdc;

public class CdcStrategyFactory {
	public static CdcStrategy create(String jdbcUrl, String user, String password) {
		String cdcStrategy = System.getenv().getOrDefault("CDC_STRATEGY", "native");
		return switch (cdcStrategy.toLowerCase()) {
			case "native" -> new NativeCdcStrategy(jdbcUrl, user, password);
			case "polling" -> new PollingCdcStrategy(jdbcUrl, user, password);
			case "trigger-outbox" -> new TriggerOutboxStrategy(jdbcUrl, user, password);
			default -> new NativeCdcStrategy(jdbcUrl, user, password);
		};
	}
}
