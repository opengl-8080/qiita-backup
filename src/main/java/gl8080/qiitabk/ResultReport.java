package gl8080.qiitabk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultReport {
    private static final Logger logger = LoggerFactory.getLogger(ResultReport.class);
    
    private static final int PROGRESS_INTERVAL = 10;
    
    private int save;
    private int error;
    private int total;
    
    public void save() {
        this.save++;
    }
    
    public void error() {
        this.error++;
    }
    
    public void total() {
        this.total++;
    }
    
    public void progress() {
        if (this.total % PROGRESS_INTERVAL == 0) {
            System.out.println(this.total);
        }
    }
    
    public void print() {
        logger.info("総ファイル数 : {}", this.total);
        logger.info("保存したファイル : {}", this.save);
        logger.info("エラーファイル : {}", this.error);
    }
}
