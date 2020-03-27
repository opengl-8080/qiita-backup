package gl8080.qiitabk.infrastructure;

import gl8080.qiitabk.infrastructure.qiita.QiitaApiToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

class QiitaApiTokenTest {
    
    private static final String TOKEN = "xyzabc123";

    @TempDir
    File tmpFolder;
    
    @BeforeEach
    void createTokenFile() throws Exception {
        Path tokenFile = new File(tmpFolder, "qiita-token").toPath();
        Files.writeString(tokenFile, TOKEN, StandardCharsets.UTF_8);
        
        System.setProperty("user.dir", tmpFolder.getAbsolutePath());
    }
    
    @Test
    void カレントフォルダのqiita_tokenというファイルの内容をトークンとして取得する() {
        // setup
        QiitaApiToken token = new QiitaApiToken();
        
        // exercise
        String tokenString = token.get();
        
        // verify
        assertThat(tokenString).isEqualTo(TOKEN);
    }

}
