package gl8080.qiitabk.infrastructure.dummy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import gl8080.qiitabk.domain.Item;
import gl8080.qiitabk.domain.ItemRepository;

@Component
@Qualifier("dummy")
public class DummyItemRepository implements ItemRepository {

    @Override
    public void save(Item item) {
        throw new NullPointerException("これはテストです。");
    }
}
