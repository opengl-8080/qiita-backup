package gl8080.qiitabk.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import gl8080.qiitabk.domain.Item;
import gl8080.qiitabk.domain.ItemList;
import gl8080.qiitabk.infrastructure.qiita.GetItemsResponse;
import gl8080.qiitabk.infrastructure.qiita.QiitaImpl;

@Component
public class ItemListImpl implements ItemList {
    private static final Logger logger = LoggerFactory.getLogger(ItemListImpl.class);
    
    private List<Item> items = new ArrayList<>();
    private static final int PER_PAGE = 5;
    private int page = 0;
    
    @Override
    public Item next() {
        return this.items.remove(0);
    }

    @Override
    public boolean hasNext() {
        if (this.items.isEmpty()) {
            this.page++;
            logger.debug("items is empty. search items. page = {}", this.page);
            
            QiitaImpl qiita = new QiitaImpl();
            GetItemsResponse response = qiita.getNextItems(this.page, PER_PAGE);
            
            List<Item> items = response.getItems();
            logger.debug("items.isEmpty() = {}", items.isEmpty());

            this.items.addAll(items);
        } else {
            logger.debug("items is not empty.");
        }
        
        return !this.items.isEmpty();
    }
}
