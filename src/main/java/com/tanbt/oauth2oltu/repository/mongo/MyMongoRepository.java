package com.tanbt.oauth2oltu.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tanbt.oauth2oltu.entity.User;

import java.util.List;

/**
 * Need to enable MongoDb configuration in user-beans.xml
 */

@Repository
public interface MyMongoRepository extends MongoRepository<User, String> {

    List<User> findByEmailAndPassword(String email, String password);
}
