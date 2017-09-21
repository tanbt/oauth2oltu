package com.tanbt.oauth2oltu.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.tanbt.oauth2oltu.entity.User;

public class UserService {

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
        String sql = "select * from users where email=? and password=?";
        User employee = (User) jdbcTemplate
                .queryForObject(sql, new Object[] { email, password },
                        new RowMapper() {
                            public User mapRow(ResultSet rs, int rowNum)
                                    throws SQLException {
                                User employee = new User();

                                return employee;
                            }
                        });
        return employee;

    }


    public void save(User user) {
        String sql = "insert into users values(?,?,?,?,?,?)";

        jdbcTemplate.update(sql,
                new Object[] { 0, user.getEmail(), user.getPassword(),
                        user.getFirstname(), user.getLastname(), user.getOrganization() });
    }
}
