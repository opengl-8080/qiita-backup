package gl8080.qiitabk.domain;

import java.nio.file.Path;

public interface ImageRepository {
    void save(Path path, Image image);
}
