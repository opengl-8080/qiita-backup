package gl8080.qiitabk.infrastructure.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

public class HttpResponseHelper {
    
    private final HttpResponse<byte[]> response;
    
    HttpResponseHelper(HttpResponse<byte[]> response) {
        this.response = response;
    }

    public String getBody(Charset charset) {
        return new String(this.response.body(), charset);
    }
    
    public void copyTo(OutputStream out) {
        try (ByteArrayInputStream body = new ByteArrayInputStream(this.response.body())) {
            body.transferTo(out);
        } catch (IOException e) {
            throw new UncheckedIOException("レスポンスの書き込みに失敗しました", e);
        }
    }
}
