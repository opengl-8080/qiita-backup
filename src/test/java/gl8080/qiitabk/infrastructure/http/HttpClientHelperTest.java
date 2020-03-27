package gl8080.qiitabk.infrastructure.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HttpClientHelperTest {
    
    private static final String URL = "http://xxx/yyy";
    private HttpClientHelper client;
    
    @Test
    void クエリがURLに追加される() {
        // setup
        client = new HttpClientHelper(URL);
        client.query("foo", 123).query("bar", 456);
        
        // exercise
        String fullUrl = client.getFullUrl();
        
        // verify
        assertThat(fullUrl).isEqualTo(URL + "?bar=456&foo=123");
    }
    
    @Test
    void URLにすでにクエリがある場合() {
        // setup
        client = new HttpClientHelper(URL + "?foo=123");
        client.query("bar", 456);
        
        // exercise
        String fullUrl = client.getFullUrl();
        
        // verify
        assertThat(fullUrl).isEqualTo(URL + "?foo=123&bar=456");
    }

}
