package com.tanbt.oauth2oltu.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tanbt.oauth2oltu.entity.Login;
import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.service.UserService;

@Controller
public class LoginController {

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView showLogin(HttpServletRequest request,
            HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("login", new Login());
        mav.addObject("message", "Please enter your username and password.");
        return mav;
    }

    @RequestMapping(value = "/loginProcess", method = RequestMethod.POST)
    public ModelAndView loginProcess(HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("login") Login login) {
        ModelAndView mav = null;
        User user = userService.getUser(login.getEmail(), login.getPassword());
        if (null != user) {
            mav = new ModelAndView("welcome");
            mav.addObject("firstname", user.getFirstname());
        } else {
            mav = new ModelAndView("login");
            mav.addObject("message", "Username or Password is wrong!!");
        }
        return mav;
    }
}
