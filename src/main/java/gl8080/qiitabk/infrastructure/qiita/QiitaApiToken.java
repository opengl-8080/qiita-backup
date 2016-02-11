package gl8080.qiitabk.infrastructure.qiita;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

public class QiitaApiToken {

    public String get() {
        Path tokenFile = Paths.get(System.getProperty("user.dir"), "qiita-token");
        try {
            return FileUtils.readFileToString(tokenFile.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("トークンファイルの取得に失敗しました", e);
        }
    }
}
