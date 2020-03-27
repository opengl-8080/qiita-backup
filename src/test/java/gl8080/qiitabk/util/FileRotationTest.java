package gl8080.qiitabk.util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.*;

public class FileRotationTest {
    
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static final String CURRENT_FILE_NAME = "test.txt";
    private static final String ANY_TEXT = "test message file content";
    private FileRotation rotate = new FileRotation();
    private File root;
    
    @Before
    public void setup() {
        root = tmp.getRoot();
        rotate.setDirectory(root);
        rotate.setCurrentFileName(CURRENT_FILE_NAME);
    }
    
    @Test
    public void 現在のファイルが次のファイルにリネームされること() throws Exception{
        // setup
        File currentFile = writeCurrentFile();
        
        // exercise
        rotate.rotate();
        
        // verify
        assertThat(currentFile)
            .as("現在のファイルが削除されていること")
            .doesNotExist();
        
        assertThat(new File(root, CURRENT_FILE_NAME + ".1"))
            .as("現在のファイルがリネームされていること")
            .exists()
            .isFile()
            .hasContent(ANY_TEXT);
    }
    
    @Test
    public void 現在のファイルが存在しない場合はなにもしない() throws Exception{
        // exercise
        rotate.rotate();
        
        // verify
        assertThat(root.listFiles()).isEmpty();
    }
    
    @Test
    public void ローテーション後のファイルはあるが_現在のファイルが存在しない場合もなにもしない() throws Exception{
        // setup
        File rotatedFile = writeRotatedFile(1);
        
        // exercise
        rotate.rotate();
        
        // verify
        assertThat(rotatedFile).exists().hasContent(ANY_TEXT);
    }
    
    @Test
    public void ローテーション済みのファイルが存在する場合は_それらのファイルもリネームされること() throws Exception{
        // setup
        File currentFile = writeCurrentFile("current");
        File rotatedFile1 = writeRotatedFile(1, "first");
        File rotatedFile2 = writeRotatedFile(2, "second");
        
        // exercise
        rotate.rotate();
        
        // verify
        assertThat(currentFile).as("現在のファイルは削除されている").doesNotExist();
        assertThat(rotatedFile1).as("現在のファイルが .1 にリネーム").hasContent("current");
        assertThat(rotatedFile2).as(".1 が .2 にリネーム").hasContent("first");
        assertThat(new File(root, CURRENT_FILE_NAME + ".3")).as(".2 が .3 にリネーム").hasContent("second");
    }
    
    @Test
    public void ローテーション後のファイル数が閾値を超えている場合は_閾値を超えているファイルが全て削除されること() throws Exception{
        // setup
        rotate.setMaxRotateNumber(3);
        
        writeCurrentFile();
        writeRotatedFile(1);
        writeRotatedFile(2);
        File rotatedFile3 = writeRotatedFile(3);
        File rotatedFile4 = writeRotatedFile(4);
        
        // exercise
        rotate.rotate();
        
        // verify
        assertThat(rotatedFile3).as(".3 は削除されていない").exists();
        assertThat(rotatedFile4).as(".4 は削除されている").doesNotExist();
        assertThat(new File(root, CURRENT_FILE_NAME + ".5")).as(".5 もない").doesNotExist();
    }
    
    private File writeRotatedFile(int rotationNumber) {
        return writeRotatedFile(rotationNumber, ANY_TEXT);
    }
    
    private File writeRotatedFile(int rotationNumber, String content) {
        return writeFile(root, CURRENT_FILE_NAME + "." + rotationNumber, content);
    }
    
    private File writeCurrentFile() {
        return writeCurrentFile(ANY_TEXT);
    }
    
    private File writeCurrentFile(String content) {
        return writeFile(root, CURRENT_FILE_NAME, content);
    }
    
    private static File writeFile(File dir, String fileName, String content) {
        File currentFile = new File(dir, fileName);
        try {
            Files.writeString(currentFile.toPath(), content, StandardCharsets.UTF_8);
            return currentFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
