package com.edacourse.api.search.infrastructure.opensearch;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class OpenSearchRepository implements SearchRepository {

	private final String baseUrl;
	private final HttpClient httpClient;

	public OpenSearchRepository() {
		this.baseUrl = System.getenv().getOrDefault("OPENSEARCH_URL", "http://localhost:9200");
		this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
	}

	@Override
	public String get(String path) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(baseUrl + path))
					.GET()
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			return response.body();

		} catch (Exception e) {
			throw new RuntimeException("Failed to GET from OpenSearch", e);
		}
	}

	@Override
	public String put(String path, String body) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(baseUrl + path))
					.PUT(HttpRequest.BodyPublishers.ofString(body))
					.header("Content-Type", "application/json")
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			return response.body();

		} catch (Exception e) {
			throw new RuntimeException("Failed to PUT to OpenSearch", e);
		}
	}

	@Override
	public String post(String path, String body) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(baseUrl + path))
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.header("Content-Type", "application/json")
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			return response.body();

		} catch (Exception e) {
			throw new RuntimeException("Failed to POST to OpenSearch", e);
		}
	}

	@Override
	public String delete(String path) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(baseUrl + path))
					.DELETE()
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			return response.body();

		} catch (Exception e) {
			throw new RuntimeException("Failed to DELETE from OpenSearch", e);
		}
	}

	@Override
	public boolean indexExists(String indexName) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(baseUrl + "/" + indexName))
					.method("HEAD", HttpRequest.BodyPublishers.noBody())
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			return response.statusCode() == 200;

		} catch (Exception e) {
			throw new RuntimeException("Failed to check if index exists in OpenSearch", e);
		}
	}

}
