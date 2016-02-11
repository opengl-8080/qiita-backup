package gl8080.qiitabk.infrastructure.qiita;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gl8080.qiitabk.domain.Item;
import gl8080.qiitabk.infrastructure.http.HttpResponse;

public class GetItemsResponse {
    
    private HttpResponse response;
    private ObjectMapper mapper = new ObjectMapper();
    
    GetItemsResponse(HttpResponse response) {
        this.response = response;
    }
    
    public int getTotalCount() {
        return this.response.getHeaderAsInt("Total-Count");
    }
    
    public List<Item> getItems() {
        try {
            return this.mapper.readValue(this.response.getBody(), new TypeReference<List<Item>>() {});
        } catch (IOException e) {
            throw new UncheckedIOException("レスポンスの解析に失敗しました。", e);
        }
    }
}
