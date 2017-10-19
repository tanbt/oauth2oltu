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

import com.tanbt.oauth2oltu.entity.OauthClient;
import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.service.OauthClientService;
import com.tanbt.oauth2oltu.service.UserService;

@Controller
public class RegistrationController {

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @Autowired
    @Qualifier("oauthClientService")
    OauthClientService oauthClientService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView showRegister(HttpServletRequest request,
            HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("user", new User());
        return mav;
    }

    @RequestMapping(value = "/registerProcess", method = RequestMethod.POST)
    public ModelAndView addUser(HttpServletRequest request,
            HttpServletResponse response, @ModelAttribute("user") User user) {
        userService.save(user);
        return new ModelAndView("welcome", "firstname", user.getFirstname());
    }

    @RequestMapping(value = "/oauth/client-register", method = RequestMethod.GET)
    public ModelAndView showOauthClientRegister(HttpServletRequest request,
            HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("oauth-client-register");
        return mav;
    }

    @RequestMapping(value = "/oauth/client-register/process", method
            = RequestMethod.POST)
    public ModelAndView addOauthClient(HttpServletRequest request,
            HttpServletResponse response) {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String app_name = request.getParameter("app_name");
        String client_uri = request.getParameter("client_uri");
        String redirect_uri = request.getParameter("redirect_uri");
        String grant_types = request.getParameter("grant_types");

        User user = userService.getUser(email, password);
        if (user != null) {

            // TODO: use your own way to generate client id and secret
            OauthClient client = oauthClientService.save(new OauthClient(
                    email + "_id", email + "_secret", app_name,
                    grant_types, client_uri, redirect_uri, user.getId()
            ));
            return new ModelAndView("empty", "message", "Thank you for " +
                    "using our service.<br/> This is your oauth client " +
                    "information for Authenciation. Please keep it safe" +
                    ".<br/><b>" + client.toString() + "</b>");
        } else {
            return new ModelAndView("empty", "message", "Wrong username or" +
                    " password");
        }
    }

}
