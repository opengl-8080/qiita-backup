package gl8080.qiitabk.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SaveImageService {
    private static final Logger logger = LoggerFactory.getLogger(SaveImageService.class);
    
    private final ImageRepository imageRepository;

    @Value("${save.dir}")
    private File saveDir;

    @Autowired
    public SaveImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * 指定した記事の画像を保存する.
     * @param item 記事
     */
    public void saveImages(Item item) {
        try {
            List<Image> imageList = item.getImageList();
            boolean requested = false;
            for (Image image : imageList) {
                Path imagePath = this.resolveImagePath(item, image);
                
                if (Files.exists(imagePath)) {
                    logger.debug("Skipping to save an image because " + imagePath + " already exists.");
                    continue;
                }

                if (requested) {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(3));
                }
                this.imageRepository.save(imagePath, image);
                requested = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private Path resolveImagePath(Item item, Image image) {
        String itemId = item.getId();
        Path imageDir = new File(this.saveDir, itemId).toPath();
        if (Files.notExists(imageDir)) {
            try {
                Files.createDirectories(imageDir);
            } catch (IOException e) {
                throw new UncheckedIOException("画像を保存するディレクトリの作成に失敗しました", e);
            }
        }

        return imageDir.resolve(image.getFileName());
    }
}
