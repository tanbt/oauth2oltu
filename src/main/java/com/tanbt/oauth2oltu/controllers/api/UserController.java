package com.tanbt.oauth2oltu.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.service.UserService;

@RestController
public class UserController {

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @RequestMapping(value = "/api/test", method = RequestMethod.GET, produces = "application/json")
    public String getTest() {
        return "{\"result\": \"success\"}";
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET, headers = "Accept=application/json")
    public List<User> getUsers() {
        return userService.findAll();
    }

}
