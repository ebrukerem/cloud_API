package com.urlshortener.urlshortener.repositories;
import com.urlshortener.urlshortener.models.Urls;
import org.bson.BSON;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface URLRepository extends MongoRepository<Urls, String> {
    Urls findBy_id(ObjectId _id);

    @Query("{ 'URL' : ?0 }")
    List<Urls> findByUrl(String url);

    @Query("{ 'hash' : ?0 }")
    List<Urls> findByHashing(String hash);

    @Query("{ 'userMail' : ?0 }")
    List<Urls> findByUsermail(String email);

    @Query("{ 'userMail' : ?0 }")
    List<Urls> findByOrderByNoOfClickDesc(String email);

    List<Urls> findTop10ByOrderByNoOfClickDesc();
}

