package com.tanbt.oauth2oltu.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.tanbt.oauth2oltu.entity.Login;
import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.service.UserService;
import com.tanbt.oauth2oltu.utils.OauthUtils;

@Controller
public class LoginController {

    private String[] requiredParamters = new String[]{"redirect_uri", "scope",
            "response_type", "client_id"};

    private Map<String, String[]> parameters = null;

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showHome() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping(value = "/oauth2/login", method = RequestMethod.GET)
    public ModelAndView showLogin(HttpServletRequest request,
            HttpServletResponse response) {

        if(!isValidOauthGetRequest(request)) {
            ModelAndView mav = new ModelAndView("empty");
            mav.addObject("message", "This login page must be accessed from" +
                    " a client website for Oauth2.");
            return mav;
        }

        parameters = request.getParameterMap();
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("login", new Login());
        String msg = request.getParameter("msg");
        if (msg != null) {
            mav.addObject("message", "Wrong username or password.");
        }
        return mav;

    }

    @RequestMapping(value = "/oauth2/loginProcess", method = RequestMethod.POST)
    public RedirectView loginProcess(HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("login") Login login) {
        ModelAndView mav = null;
        User user = userService.getUser(login.getEmail(), login.getPassword());
        if (null != user) {
            return OauthUtils.redirect(parameters.get("redirect_uri")[0]);
        } else {
            return OauthUtils.redirect(request.getHeader("referer") +
                    "&msg=login-failed");
        }
    }

    private boolean isValidOauthGetRequest(HttpServletRequest req) {
        for (String para : requiredParamters) {
            if (req.getParameter(para) == null ) {
                return false;
            }
        }
        return true;
    }

}
