package gl8080.qiitabk.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import gl8080.qiitabk.domain.Item;
import gl8080.qiitabk.domain.ItemRepository;
import gl8080.qiitabk.util.FileNameNormalizer;
import gl8080.qiitabk.util.FileRotation;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    private static final int MAX_ROTATE_NUMBER = 5;
    
    @Value("${save.dir}")
    private File saveDir;
    
    @Override
    public void save(Item item) {
        String fileName = FileNameNormalizer.normalize(item.getTitle());
        logger.debug("file name = {}", fileName);
        
        try {
            logger.debug("save dir = " + this.saveDir);
            File saveFile = new File(this.saveDir, fileName + ".md");
            
            if (saveFile.exists() && saveFile.isFile()) {
                byte[] localHash;
                try (InputStream in = new FileInputStream(saveFile)) {
                    localHash = DigestUtils.md5Digest(in);
                }
                byte[] downloadHash = DigestUtils.md5Digest(item.getText().getBytes());
                
                if (!Arrays.equals(localHash, downloadHash)) {
                    this.rotateFile(saveFile);
                    this.saveFile(item, saveFile);
                } else {
                    logger.debug("hash are same.");
                }
            } else {
                logger.debug("local file dosen't exists.");
                this.saveFile(item, saveFile);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("ファイルの保存に失敗しました。", e);
        }
    }

    private void saveFile(Item item, File saveFile) throws IOException {
        FileUtils.writeStringToFile(saveFile, item.getText(), StandardCharsets.UTF_8);
        logger.info("{} に保存しました。", saveFile.getAbsolutePath());
    }

    private void rotateFile(File saveFile) {
        logger.debug("rotate files. current file name = {}", saveFile.getName());
        FileRotation rotation = new FileRotation();
        rotation.setDirectory(this.saveDir);
        rotation.setCurrentFileName(saveFile.getName());
        rotation.setMaxRotateNumber(MAX_ROTATE_NUMBER);
        rotation.rotate();
    }
}
