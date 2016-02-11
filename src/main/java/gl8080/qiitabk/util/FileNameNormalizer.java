package gl8080.qiitabk.util;

import java.util.regex.Pattern;

public class FileNameNormalizer {
    
    private static final Pattern UNUSABLE_CHARACTERS_PATTERN = Pattern.compile("[\\\\/:*?\"<>|]");
    
    public static String normalize(String string) {
        return UNUSABLE_CHARACTERS_PATTERN.matcher(string).replaceAll("_");
    }
}
