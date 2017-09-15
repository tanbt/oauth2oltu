package com.tanbt.oauth2oltu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.repository.mysql.MySQLRepository;

public class UserService {

    //todo: error: this is MongoDB repo
    @Autowired
    private MySQLRepository mySQLRepository;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
     *
     * @param email
     * @param password
     * @return
     */
    public User getUser(String email, String password) {
        return mySQLRepository.findByEmailAndPassword(email, password).get(0);
    }


    public User save(User user) {
        return mySQLRepository.save(user);
    }
}
