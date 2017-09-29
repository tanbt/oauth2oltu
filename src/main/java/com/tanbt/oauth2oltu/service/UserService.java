package com.tanbt.oauth2oltu.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.repository.mongo.MyMongoRepository;

public class UserService {

    @Autowired
    private MyMongoRepository repo;

    /**
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
     */
    public User getUser(String email, String password) {
        return repo.findByEmailAndPassword(email, password).get(0);
    }


    public User save(User user) {
        return repo.save(user);
    }
}
