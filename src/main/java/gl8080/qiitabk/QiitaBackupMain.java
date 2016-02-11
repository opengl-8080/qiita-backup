package gl8080.qiitabk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import gl8080.qiitabk.domain.Item;
import gl8080.qiitabk.domain.ItemList;
import gl8080.qiitabk.domain.ItemRepository;
import gl8080.qiitabk.domain.Qiita;

@SpringBootApplication
public class QiitaBackupMain {
    private static final Logger logger = LoggerFactory.getLogger(QiitaBackupMain.class);
    
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(QiitaBackupMain.class);
        app.setBannerMode(Banner.Mode.OFF);
        
        try (ConfigurableApplicationContext ctx = app.run(args)) {
            ctx.getBean(QiitaBackupMain.class).execute();
        }
    }
    
    @Autowired
    private ItemRepository repository;
    
    @Autowired
    private Qiita qiita;
    
    private void execute() {
        ItemList itemList = this.qiita.getItemList();
        
        while (itemList.hasNext()) {
            String title = null;
            try {
                Item item = itemList.next();
                title = item.getTitle();
                this.repository.save(item);
            } catch (Exception e) {
                logger.error(title + " の処理中にエラーが発生しました。", e);
            }
        }
    }
}
