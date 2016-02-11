package gl8080.qiitabk.infrastructure.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    
    private String url;
    private String method;
    private Map<String, Object> queries = new HashMap<>();
    private Map<String, Object> headers = new HashMap<>();
    
    public HttpClient url(String url) {
        Objects.requireNonNull(url);
        this.url = url;
        return this;
    }

    public HttpClient query(String key, int value) {
        this.queries.put(key, value);
        return this;
    }

    public HttpClient method(String method) {
        Objects.requireNonNull(method);
        this.method = method;
        return this;
    }

    public HttpClient header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public HttpResponse connect() {
        try {
            URL url = new URL(this.getFullUrl());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            con.setRequestMethod(this.method);
            this.headers.forEach((key, value) -> {
                con.setRequestProperty(key, String.valueOf(value));
            });
            
            logger.debug("connect to {}", this.url);
            con.connect();
            
            return new HttpResponse(con);
        } catch (IOException e) {
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
