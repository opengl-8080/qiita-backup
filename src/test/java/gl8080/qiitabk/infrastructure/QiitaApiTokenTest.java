package gl8080.qiitabk.infrastructure;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import gl8080.qiitabk.infrastructure.qiita.QiitaApiToken;

public class QiitaApiTokenTest {
    
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();
    
    private Path tokenFile;
    
    private static final String TOKEN = "xyzabc123";
    
    @Before
    public void createTokenFile() throws Exception {
        File root = tmpFolder.getRoot();
        tokenFile = new File(root, "qiita-token").toPath();
        FileUtils.write(tokenFile.toFile(), TOKEN);
        
        System.setProperty("user.dir", root.getAbsolutePath());
    }
    
    @Test
    public void カレントフォルダのqiita_tokenというファイルの内容をトークンとして取得する() {
        // setup
        QiitaApiToken token = new QiitaApiToken();
        
        // exercise
        String tokenString = token.get();
        
        // verify
        assertThat(tokenString).isEqualTo(TOKEN);
    }

}
