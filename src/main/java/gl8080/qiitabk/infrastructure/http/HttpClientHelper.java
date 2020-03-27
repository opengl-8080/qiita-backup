package gl8080.qiitabk.infrastructure.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpClientHelper {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);
    
    private final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
    private final Map<String, Object> queries = new HashMap<>();
    private final String url;

    public HttpClientHelper(String url) {
        this.url = Objects.requireNonNull(url);
    }
    
    public HttpClientHelper GET() {
        this.requestBuilder.GET();
        return this;
    }

    public HttpClientHelper query(String key, int value) {
        this.queries.put(key, value);
        return this;
    }

    public HttpClientHelper header(String key, String value) {
        this.requestBuilder.header(key, value);
        return this;
    }

    public HttpResponseHelper send() {
        try {
            URI uri = URI.create(this.getFullUrl());
            this.requestBuilder.uri(uri);

            logger.debug("connect to {}", this.url);

            HttpResponse<byte[]> response = HttpClient.newHttpClient()
                    .send(requestBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

            return new HttpResponseHelper(response);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    String getFullUrl() {
        StringBuilder fullUrl = new StringBuilder(this.url);
        
        if (this.url.contains("?")) {
            fullUrl.append("&");
        } else {
            fullUrl.append("?");
        }
        
        return fullUrl + this.queries.entrySet().stream()
                                .map(e -> e.getKey() + "=" + e.getValue())
                                .collect(Collectors.joining("&"));
    }
}
