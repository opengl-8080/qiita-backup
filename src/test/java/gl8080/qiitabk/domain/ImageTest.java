package gl8080.qiitabk.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class ImageTest {

    private String fileName = "02fcda63-1759-213c-64eb-ec4273efe545.jpeg";
    private String url = "https://qiita-image-store.s3.amazonaws.com/0/28302/" + fileName;
    
    @Test
    public void urlのチェック() throws Exception {
        // setup
        Image image = new Image(url);
        
        // verify
        assertThat(image.getUrl()).isEqualTo(url);
    }

    @Test
    public void fileNameはurlから解決する() throws Exception {
        // setup
        Image image = new Image(url);

        // verify
        assertThat(image.getFileName()).isEqualTo(fileName);
    }
}