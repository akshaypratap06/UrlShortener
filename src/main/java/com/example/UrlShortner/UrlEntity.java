package com.example.UrlShortner;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name ="tiny-url")
public class UrlEntity {

    @Id
    @Column(name = "id")
    @JsonProperty("shortUrl")
    private String id;

    @Column(name = "url")
    private String url;

    public UrlEntity(String id, String url) {
        this.id = id;
        this.url = url;
    }
    public UrlEntity(){

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlEntity urlEntity = (UrlEntity) o;
        return Objects.equals(id, urlEntity.id) && Objects.equals(url, urlEntity.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void prepend(){
        this.id="www.tinyurl.com/"+this.id;
    }
}
