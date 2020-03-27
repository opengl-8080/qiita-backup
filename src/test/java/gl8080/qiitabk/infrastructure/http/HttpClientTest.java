package gl8080.qiitabk.infrastructure.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HttpClientTest {
    
    private static final String URL = "http://xxx/yyy";
    private HttpClient client = new HttpClient();
    
    @Test
    void クエリがURLに追加される() {
        // setup
        client.url(URL).query("foo", 123).query("bar", 456);
        
        // exercise
        String fullUrl = client.getFullUrl();
        
        // verify
        assertThat(fullUrl).isEqualTo(URL + "?bar=456&foo=123");
    }
    
    @Test
    void URLにすでにクエリがある場合() {
        // setup
        client.url(URL + "?foo=123").query("bar", 456);
        
        // exercise
        String fullUrl = client.getFullUrl();
        
        // verify
        assertThat(fullUrl).isEqualTo(URL + "?foo=123&bar=456");
    }

}
