package com.example.UrlShortner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class UrlSaver {

    @PersistenceContext
    EntityManager entityManager;
    @Transactional
    public UrlEntity save(UrlEntity url){
        Session s= entityManager.unwrap(Session.class);
        s.merge(url);
        return url;
    }
@Transactional
    public Optional<UrlEntity> getUrl(String url) {
        Session s= entityManager.unwrap(Session.class);
        return Optional.ofNullable(s.get(UrlEntity.class,url));
    }
}
