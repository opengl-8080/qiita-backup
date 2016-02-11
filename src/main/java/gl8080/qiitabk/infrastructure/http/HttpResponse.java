package gl8080.qiitabk.infrastructure.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public class HttpResponse {
    
    private HttpURLConnection con;
    
    HttpResponse(HttpURLConnection con) {
        this.con = con;
    }

    public int getHeaderAsInt(String key) {
        return Integer.valueOf(this.con.getHeaderField(key));
    }

    public String getBody() {
        try (InputStream in = this.con.getInputStream()) {
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("レスポンスの取得に失敗しました。", e);
        }
    }
}
