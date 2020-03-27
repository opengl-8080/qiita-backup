package gl8080.qiitabk.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FileRotation {
    
    private File dir;
    private String currentFileName;
    private int maxRotateNumber = Integer.MAX_VALUE;
    
    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public void setDirectory(File dir) {
        this.dir = dir;
    }

    public void setMaxRotateNumber(int maxRotateNumber) {
        this.maxRotateNumber = maxRotateNumber;
    }

    public void rotate() {
        File currentFile = new File(this.dir, this.currentFileName);
        
        if (currentFile.isFile() && currentFile.exists()) {
            this.rotateRotatedFiles();
            this.rotateCurrentFile(currentFile);
            this.removeOlderFiles();
        }
    }

    /**
     * 現在のファイルをローテーションする。
     */
    private void rotateCurrentFile(File currentFile) {
        File nextFile = new File(this.dir, this.currentFileName + ".1");
        this.rename(currentFile, nextFile);
    }

    /**
     * ローテーション番号が閾値を超えているファイルを削除する。
     */
    private void removeOlderFiles() {
        Stream
            .of(this.dir.listFiles())
            .filter(this::byMatchedCurrentFileNamePattern)
            .filter(this::byOverMaxRotateNumber)
            .forEach(File::delete);
    }
    
    /**
     * ローテーション番号が閾値を超えているファイルだけに絞る。
     */
    private boolean byOverMaxRotateNumber(File file) {
        return this.maxRotateNumber < this.getFileNumber(file);
    }
    
    /**
     * ローテーション済みのファイルをローテーションする。
     */
    private void rotateRotatedFiles() {
        Stream
            .of(this.dir.listFiles())
            .filter(this::byMatchedCurrentFileNamePattern)
            .sorted(this::byFileNumberDesc)
            .forEach(this::incrementFileNumber)
        ;
    }

    /**
     * ローテーション後のファイルも含めて、対象のファイルを絞り込む。
     */
    private boolean byMatchedCurrentFileNamePattern(File file) {
       return file.getName().replace(this.currentFileName, "").matches("^\\.\\d+$");
    }

    /**ローテーションファイルからローテーション番号を取得するためのパターン*/
    private static final Pattern FILE_NUMBER_PATTERN = Pattern.compile("\\d+$");
    
    /**
     * ローテーション番号で降順にソートする。
     */
    private int byFileNumberDesc(File f1, File f2) {
        return this.getFileNumber(f2) - this.getFileNumber(f1);
    }
    
    /**
     * ローテーション番号を抽出する。
     */
    private int getFileNumber(File file) {
        Matcher matcher = FILE_NUMBER_PATTERN.matcher(file.getName());
        matcher.find();
        String number = matcher.group();
        return Integer.parseInt(number);
    }
    
    /**
     * ローテーション番号を１つ加算する。
     */
    private void incrementFileNumber(File file) {
        int fileNumber = this.getFileNumber(file);
        File nextFile = new File(this.dir, this.currentFileName + "." + (fileNumber + 1));
        
        this.rename(file, nextFile);
    }
    
    /**
     * ファイル名を変更する。
     */
    private void rename(File from, File to) {
        try {
            Files.move(from.toPath(), to.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException("ファイルのローテーションに失敗しました。", e);
        }
    }
}
