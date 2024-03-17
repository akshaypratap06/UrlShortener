package com.example.UrlShortner;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
public class UrlShortnerResource {

    @Autowired
    UrlSaver urlSaver;

    public static Cache<String, UrlEntity> cache;

    @PostConstruct
    void init() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
    }

    @GetMapping("/short/{url}")
    public ResponseEntity<Object> getTinyUrl(@PathVariable String url) {
        UrlEntity cacheUrl = cache.getIfPresent(url);
        if (cacheUrl != null) {
            return ResponseEntity.ok().header("Location",cacheUrl.getUrl()).build();
        }
        Optional<UrlEntity> urlEntity = urlSaver.getUrl(url);
        if (urlEntity.isPresent()) {
            final UrlEntity urlEntityResponse= urlEntity.get();
            cache.put(urlEntityResponse.getId(), urlEntityResponse);
            return ResponseEntity.ok().header("Location",urlEntityResponse.getUrl()).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/long/{url}")
    public ResponseEntity<Object> getLongUrl(@PathVariable String url) {
        String base64EncodedUrl = Base64.getUrlEncoder().encodeToString(url.getBytes());
        Optional<UrlEntity> urlEntity = urlSaver.getUrl(base64EncodedUrl);
        if (urlEntity.isPresent()) {
            return ResponseEntity.ok("Already Present");
        } else {
            UrlEntity urlEntityResponse = new UrlEntity(base64EncodedUrl, url);
            urlSaver.save(urlEntityResponse);
            cache.put(urlEntityResponse.getId(),urlEntityResponse);
            urlEntityResponse.prepend();
            return ResponseEntity.ok(urlEntityResponse);
        }
    }
}
