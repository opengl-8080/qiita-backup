package gl8080.qiitabk;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gl8080.qiitabk.response.Item;

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
            
            URL url = new URL("https://qiita.com/api/v2/authenticated_user/items?per_page=20&page=2");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + token);
            
            con.connect();
            
            try (InputStream in = con.getInputStream()) {
                String json = IOUtils.toString(in, StandardCharsets.UTF_8);
                
                ObjectMapper mapper = new ObjectMapper();
                List<Item> items = mapper.readValue(json, new TypeReference<List<Item>>(){});
                
                items.forEach(System.out::println);
                
                String totalCount = con.getHeaderField("Total-Count");
                System.out.println(totalCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
