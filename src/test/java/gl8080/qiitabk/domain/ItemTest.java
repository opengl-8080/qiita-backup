package gl8080.qiitabk.domain;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ItemTest {
    
    private final String URL_BASE = "https://qiita-image-store.s3.amazonaws.com/0/28302/";
    
    @Test
    public void text内の画像を全て取得できる() throws Exception {
        // setup
        String text =
                "![hoge](" + URL_BASE + "image1.jpeg)\n" +
                "#foo\n" +
                "![]{test}\n" +
                "!()[foobar]\n" +
                "![](" + URL_BASE + "image2.png)\n" +
                "![test](" + URL_BASE + "image3.gif)\n" +
                "![xxx] (" + URL_BASE + "no.jpeg)";
        
        Item item = new Item("id", "title", text);
        
        // exercise
        List<Image> imageList = item.getImageList();
        
        // verify
        assertThat(imageList)
            .extracting(Image::getFileName)
            .containsExactly(
                "image1.jpeg",
                "image2.png",
                "image3.gif"
            );
    }
}