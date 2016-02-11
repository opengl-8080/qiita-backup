package gl8080.qiitabk.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

public class FileNameNormalizerTest {

    @Test
    public void Windowsで使用できない文字は_全て半角アンダーバーに変換される() {
        // exercise
        String normalized = FileNameNormalizer.normalize("\\/:*?\"<>|");
        
        // verify
        assertThat(normalized).isEqualTo("_________");
    }

    @Test
    public void 使用できる文字は変換されないこと() {
        // exercise
        String normalized = FileNameNormalizer.normalize("!#$%&'()=~-^@`[{+;}],._ ");
        
        // verify
        assertThat(normalized).isEqualTo("!#$%&'()=~-^@`[{+;}],._ ");
    }

}
