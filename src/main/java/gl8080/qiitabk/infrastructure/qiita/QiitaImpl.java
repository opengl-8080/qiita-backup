package gl8080.qiitabk.infrastructure.qiita;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import gl8080.qiitabk.domain.ItemList;
import gl8080.qiitabk.domain.Qiita;
import gl8080.qiitabk.infrastructure.ItemListImpl;
import gl8080.qiitabk.infrastructure.http.HttpClient;
import gl8080.qiitabk.infrastructure.http.HttpResponse;

@Component
public class QiitaImpl implements Qiita {
    private static final Logger logger = LoggerFactory.getLogger(QiitaImpl.class);
    
    private static long WAIT_MILLISEC = TimeUnit.SECONDS.toMillis(5);
    private static long lastAccessTime = 0;
    
    @Override
    public ItemList getItemList() {
        return new ItemListImpl();
    }

    public GetItemsResponse getNextItems(int page, int perPage) {
        HttpClient client =
                new HttpClient()
                    .url("https://qiita.com/api/v2/authenticated_user/items")
                    .method("GET")
                    .query("per_page", perPage)
                    .query("page", page)
                    .header("Authorization", "Bearer " + new QiitaApiToken().get());
        
        this.waitTime();
        
        HttpResponse response = client.connect();
        
        lastAccessTime = System.currentTimeMillis();
        
        return new GetItemsResponse(response);
    }

    private void waitTime() {
        if (lastAccessTime == 0) {
            return;
        }
        
        try {
            long now;
            do {
                now = System.currentTimeMillis();
                
                logger.debug("sleep... now={}, lastAccessTime={}, diff={}", now, lastAccessTime, now - lastAccessTime);
                
                Thread.sleep(1000);
                
            } while (now - lastAccessTime < WAIT_MILLISEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
