package com.test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.Stream;

public class Test {

	public static void main(String[] args) throws Exception {

		String URL = "https://9x1n96dg-8090.usw3.devtunnels.ms/api/events/stream";

		System.out.println("Starting SSE client..." + URL);

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(URL)) // Replace with your SSE endpoint
				.timeout(Duration.ofSeconds(120))
				.header("Accept", "text/event-stream") // Optional, but good practice
				.build();

		HttpResponse<Stream<String>> response = client.send(request, HttpResponse.BodyHandlers.ofLines());

		// Process each line as it arrives
		try (Stream<String> linesInResponse = response.body()) {
			linesInResponse.forEach(line -> {
				// SSE events come in "data: [content]" format
				if (line.startsWith("data:")) {
					String eventData = line.substring(5).trim();
					System.out.println("Received SSE event data: " + eventData);
				}
				// Handle other SSE fields like 'event:', 'id:', 'retry:' if necessary
			});
		}
	}

}
