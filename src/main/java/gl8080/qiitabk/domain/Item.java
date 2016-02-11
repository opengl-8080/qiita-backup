package gl8080.qiitabk.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Item {
    
    private final String id;
    private final String title;
    private final String text;
    
    public Item(
            @JsonProperty("id") String id,
            @JsonProperty("title") String title,
            @JsonProperty("body") String text) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(title);
        
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", title=" + title + ", text=<...>]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Item)) return false;
        
        Item other = (Item)o;
        return this.id.equals(other.id);
    }
    
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
