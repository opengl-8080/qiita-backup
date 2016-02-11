package gl8080.qiitabk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import gl8080.qiitabk.domain.Item;
import gl8080.qiitabk.domain.ItemList;
import gl8080.qiitabk.domain.Qiita;

@SpringBootApplication
public class QiitaBackupMain {
    
    public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(QiitaBackupMain.class, args)) {
            ctx.getBean(QiitaBackupMain.class).test();
        }
    }
    
    @Autowired
    private Qiita qiita;
    
    private void test() {
        ItemList itemList = this.qiita.getItemList();
        
        while (itemList.hasNext()) {
            Item item = itemList.next();
            System.out.println(item);
        }
    }
}
