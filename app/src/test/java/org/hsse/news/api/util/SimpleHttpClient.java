package org.hsse.news.api.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class SimpleHttpClient {
    private static final HttpResponse.BodyHandler<String> CHARSET_HANDLER =
            HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8);

    private final HttpClient client = HttpClient.newHttpClient();

    public HttpResponse<String> get(final String url) throws IOException, InterruptedException {
        return client.send(
                getBaseBuilder(url)
                        .GET()
                        .build(),
                CHARSET_HANDLER
        );
    }

    public HttpResponse<String> post(final String url, final String body)
            throws IOException, InterruptedException {
        return client.send(
                getBaseBuilder(url)
                        .POST(BodyPublishers.ofString(body))
                        .build(),
                CHARSET_HANDLER
        );
    }

    public HttpResponse<String> put(final String url, final String body)
            throws IOException, InterruptedException {
        return client.send(
                getBaseBuilder(url)
                        .PUT(BodyPublishers.ofString(body))
                        .build(),
                CHARSET_HANDLER
        );
    }

    public HttpResponse<String> delete(final String url) throws IOException, InterruptedException {
        return client.send(
                getBaseBuilder(url)
                        .DELETE()
                        .build(),
                CHARSET_HANDLER
        );
    }

    private HttpRequest.Builder getBaseBuilder(final String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .setHeader("Content-Type", "application/json");
    }
}
