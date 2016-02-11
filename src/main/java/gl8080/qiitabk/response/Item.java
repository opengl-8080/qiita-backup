package gl8080.qiitabk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Item {
    
    public String id;
    public String title;
    public String url;
    public String body;
    
    @Override
    public String toString() {
        return "Item [id=" + id + ", title=" + title + ", url=" + url + ", body=<...>]";
    }
}
