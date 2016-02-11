package gl8080.qiitabk;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class QiitaBackupMain {
    
    public static void main(String[] args) {
        try (ConfigurableApplicationContext ctx = SpringApplication.run(QiitaBackupMain.class, args)) {
            execute();
        }
    }
    
    private static void execute() {
        try {
            String token = new String(Files.readAllBytes(Paths.get("qiita-token")));
            
            URL url = new URL("https://qiita.com/api/v2/authenticated_user");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + token);
            
            con.connect();
            
            try (InputStream in = con.getInputStream()) {
                Path tmp = Paths.get("./tmp");
                Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
                
                try (Stream<String> s = Files.lines(tmp)) {
                    s.forEach(System.out::println);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
