package com.tanbt.oauth2oltu.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tanbt.oauth2oltu.entity.User;

import java.util.List;

@Repository
public interface MyMongoRepository extends MongoRepository<User, String> {

    List<User> findByEmailAndPassword(String email, String password);
}
