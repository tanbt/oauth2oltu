package com.tanbt.oauth2oltu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.repository.mysql.UserRepository;

/**
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
public class UserService {

    /**
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
     */
    @Autowired
    private UserRepository repo;

    public User getUser(String email, String password) {
        try {
            return repo.findByEmailAndPassword(email, password).get(0);
        } catch (Exception ex) {
            return null;
        }

    }

    public User findById(int id) {
        return repo.findById(id).get(0);
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public User save(User user) {
        return repo.save(user);
    }
}
