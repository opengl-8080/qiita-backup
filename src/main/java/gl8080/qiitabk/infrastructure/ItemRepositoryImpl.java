package gl8080.qiitabk.infrastructure;

import gl8080.qiitabk.domain.Item;
import gl8080.qiitabk.domain.ItemRepository;
import gl8080.qiitabk.util.DefaultQualifier;
import gl8080.qiitabk.util.FileNameNormalizer;
import gl8080.qiitabk.util.FileRotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

@Component
@DefaultQualifier
public class ItemRepositoryImpl implements ItemRepository {
    private static final Logger logger = LoggerFactory.getLogger(ItemRepositoryImpl.class);

    private static final boolean SAVED = true;
    private static final int MAX_ROTATE_NUMBER = 5;
    
    @Value("${save.dir}")
    private File saveDir;
    
    @Override
    public boolean save(Item item) {
        String fileName = FileNameNormalizer.normalize(item.getTitle() + "_" + item.getId());
        logger.debug("file name = {}", fileName);
        
        try {
            logger.debug("save dir = " + this.saveDir);
            File saveFile = new File(this.saveDir, fileName + ".md");
            
            if (saveFile.exists() && saveFile.isFile()) {
                if (!this.areSameContents(saveFile, item)) {
                    this.rotateFile(saveFile);
                    this.saveFile(item, saveFile);
                    return SAVED;
                } else {
                    logger.debug("hash are same.");
                    return !SAVED;
                }
            } else {
                logger.debug("local file dosen't exists.");
                this.saveFile(item, saveFile);
                return SAVED;
            }
        } catch (IOException e) {
            throw new UncheckedIOException("ファイルの保存に失敗しました。", e);
        }
    }
    
    private boolean areSameContents(File file, Item item) {
        byte[] localHash = this.getHash(file);
        byte[] downloadHash = DigestUtils.md5Digest(item.getText().getBytes(StandardCharsets.UTF_8));
        
        if (logger.isDebugEnabled()) {
            logger.debug("local = " + DigestUtils.md5DigestAsHex(localHash));
            logger.debug("download = " + DigestUtils.md5DigestAsHex(downloadHash));
        }
        
        return Arrays.equals(localHash, downloadHash);
    }
    
    private byte[] getHash(File file) {
        try (InputStream in = new FileInputStream(file)) {
            return DigestUtils.md5Digest(in);
        } catch (IOException e) {
            throw new UncheckedIOException("ハッシュの取得に失敗しました。", e);
        }
    }

    private void saveFile(Item item, File saveFile) throws IOException {
        Files.writeString(saveFile.toPath(), item.getText(), StandardCharsets.UTF_8);
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
