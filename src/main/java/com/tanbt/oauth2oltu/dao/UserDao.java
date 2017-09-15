package com.tanbt.oauth2oltu.dao;


import com.tanbt.oauth2oltu.model.Login;
import com.tanbt.oauth2oltu.model.User;

public interface UserDao {
    void register(User user);
    User validateUser(Login login);
}
