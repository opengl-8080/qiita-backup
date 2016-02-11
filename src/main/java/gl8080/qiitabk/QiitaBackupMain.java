package gl8080.qiitabk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class QiitaBackupMain {
    
    public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(QiitaBackupMain.class, args)) {
            
        }
    }
}
