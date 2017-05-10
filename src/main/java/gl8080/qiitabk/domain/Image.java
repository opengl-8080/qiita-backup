package gl8080.qiitabk.domain;

import java.util.Objects;

public class Image {
    private final String url;
    private final String fileName;

    public Image(String url) {
        this.url = Objects.requireNonNull(url);
        String[] paths = this.url.split("/");
        this.fileName = paths[paths.length - 1];
    }

    public String getUrl() {
        return this.url;
    }

    public String getFileName() {
        return this.fileName;
    }
}
