package com.tanbt.oauth2oltu.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tanbt.oauth2oltu.entity.User;

@Repository
public interface MyMongoRepository extends MongoRepository<User, String> {

}
