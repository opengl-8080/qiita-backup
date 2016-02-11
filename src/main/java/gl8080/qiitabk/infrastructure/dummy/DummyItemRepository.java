package gl8080.qiitabk.infrastructure.dummy;

import org.springframework.stereotype.Component;

import gl8080.qiitabk.domain.Item;
import gl8080.qiitabk.domain.ItemRepository;
import gl8080.qiitabk.util.DummyQualifier;

@Component
@DummyQualifier
public class DummyItemRepository implements ItemRepository {

    @Override
    public boolean save(Item item) {
        throw new NullPointerException("これはテストです。");
    }
}
