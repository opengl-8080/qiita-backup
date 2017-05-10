package gl8080.qiitabk;

import gl8080.qiitabk.domain.SaveImageService;
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
import gl8080.qiitabk.util.DefaultQualifier;

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
    
    @Autowired @DefaultQualifier
    private ItemRepository repository;
    
    @Autowired
    private Qiita qiita;
    @Autowired
    private SaveImageService saveImageService;
    
    private void execute() {
        ResultReport report = new ResultReport();
        
        try {
            ItemList itemList = this.qiita.getItemList();
            while (itemList.hasNext()) {
                report.total();
                
                Item item = itemList.next();
                try {
                    if (this.repository.save(item)) {
                        this.saveImageService.saveImages(item);
                        report.save();
                    }
                } catch (Exception e) {
                    logger.error(item.getTitle() + " の処理中にエラーが発生しました。", e);
                    report.error();
                } finally {
                    report.progress();
                }
            }
        } finally {
            report.print();
        }
    }
}
