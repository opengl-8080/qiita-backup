package gl8080.qiitabk.infrastructure;

import gl8080.qiitabk.domain.Image;
import gl8080.qiitabk.domain.ImageRepository;
import gl8080.qiitabk.infrastructure.http.HttpClient;
import gl8080.qiitabk.infrastructure.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ImageRepositoryImpl implements ImageRepository {
    private static final Logger logger = LoggerFactory.getLogger(ImageRepositoryImpl.class);
    
    @Override
    public void save(Path imagePath, Image image) {
        logger.info("downloading " + image.getFileName() + "...");
        HttpResponse response = new HttpClient().url(image.getUrl()).method("GET").connect();
        
        try (OutputStream out = Files.newOutputStream(imagePath)){
            response.copyTo(out);
            logger.info("Saving an image to '" + imagePath + "'.");
        } catch (IOException e) {
            throw new UncheckedIOException("画像の保存に失敗しました。", e);
        }
    }
}
