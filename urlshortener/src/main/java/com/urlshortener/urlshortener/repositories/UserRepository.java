package com.urlshortener.urlshortener.repositories;
import com.urlshortener.urlshortener.models.Urls;
import com.urlshortener.urlshortener.models.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {
    Users findBy_id(ObjectId _id);

    @Query("{ 'email' : ?0 }")
    List<Users> findByMail(String email);


}


