package com.tanbt.oauth2oltu.dao;


import com.tanbt.oauth2oltu.entity.Login;
import com.tanbt.oauth2oltu.entity.User;

public interface UserDao {
    void register(User user);
    User validateUser(Login login);
}
