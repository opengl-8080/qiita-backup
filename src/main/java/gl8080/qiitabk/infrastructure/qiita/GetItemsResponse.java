package gl8080.qiitabk.infrastructure.qiita;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gl8080.qiitabk.domain.Item;
import gl8080.qiitabk.infrastructure.http.HttpResponseHelper;

public class GetItemsResponse {
    
    private HttpResponseHelper response;
    private ObjectMapper mapper = new ObjectMapper();
    
    GetItemsResponse(HttpResponseHelper response) {
        this.response = response;
    }
    
    public List<Item> getItems() {
        try {
            return this.mapper.readValue(
                    this.response.getBody(StandardCharsets.UTF_8),
                    new TypeReference<List<Item>>() {});
        } catch (IOException e) {
            throw new UncheckedIOException("レスポンスの解析に失敗しました。", e);
        }
    }
}
