package gl8080.qiitabk.infrastructure.qiita;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QiitaApiToken {

    public String get() {
        Path tokenFile = Paths.get(System.getProperty("user.dir"), "qiita-token");
        try {
            return Files.readString(tokenFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("トークンファイルの取得に失敗しました", e);
        }
    }
}
