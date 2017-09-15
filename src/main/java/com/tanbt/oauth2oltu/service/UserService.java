package com.tanbt.oauth2oltu.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.repository.mysql.MySQLRepository;

public class UserService {

    /**
     * Inject the Db Repository
     * To use MongoDb engine:
     *  private MyMongoRepository DbRepository;
     */
    @Autowired
    private MySQLRepository DbRepository;

    /**
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
     */
    public User getUser(String email, String password) {
        return DbRepository.findByEmailAndPassword(email, password).get(0);
    }


    public User save(User user) {
        return DbRepository.save(user);
    }
}
