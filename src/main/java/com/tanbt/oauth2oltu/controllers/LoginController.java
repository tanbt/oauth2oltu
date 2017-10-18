package com.tanbt.oauth2oltu.controllers;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.tanbt.oauth2oltu.entity.Login;
import com.tanbt.oauth2oltu.entity.OauthClient;
import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.service.OauthClientService;
import com.tanbt.oauth2oltu.service.UserService;
import com.tanbt.oauth2oltu.utils.OauthUtils;

@Controller
public class LoginController {

    /**
     * Default expiration is a week
     */
    public static Long EXPIRE_DURATION = 604800l;

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @Autowired
    @Qualifier("oauthClientService")
    OauthClientService oauthClientService;

    private String[] requiredParamters = new String[] {
            OAuth.OAUTH_REDIRECT_URI, OAuth.OAUTH_SCOPE,
            OAuth.OAUTH_RESPONSE_TYPE, OAuth.OAUTH_CLIENT_ID };

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showHome() {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping(value = "/oauth2/login", method = RequestMethod.GET)
    public ModelAndView showLogin(HttpServletRequest request,
            HttpServletResponse response) {

        if (!isValidOauthGetRequest(request)) {
            ModelAndView mav = new ModelAndView("empty");
            mav.addObject("message", "This login page must be accessed from" +
                    " a client website for Oauth2.");
            return mav;
        }

        if (!isValidOauthClient(request)) {
            ModelAndView mav = new ModelAndView("empty");
            mav.addObject("message", "Oauth Client user information is " +
                    "wrong.");
            return mav;
        }

        String scope    = request.getParameter(OAuth.OAUTH_SCOPE);


        ModelAndView mav = new ModelAndView("login");
        mav.addObject("login", new Login());
        mav.addObject("parameters", request.getQueryString());
        String msg = request.getParameter("msg");
        if (msg != null) {
            mav.addObject("message", "Wrong username or password.");
        }
        return mav;

    }

    @RequestMapping(value = "/oauth2/loginProcess", method = RequestMethod.POST)
    public RedirectView loginProcess(HttpServletRequest request,
            HttpServletResponse res, @ModelAttribute("login") Login login)
            throws URISyntaxException, OAuthSystemException {
        ModelAndView mav = null;
        User user = userService.getUser(login.getEmail(), login.getPassword());
        if (null != user) {
            // TODO: attach the code/token with the user/client and save to DB
            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(
                    new MD5Generator());



            String code     = oauthIssuerImpl.authorizationCode();
            String token    = oauthIssuerImpl.accessToken();



            return OauthUtils.redirect(OauthUtils
                    .GenerateLinkAfterLogin(request, code, token, EXPIRE_DURATION));
        } else {
            return OauthUtils.redirect(
                    request.getHeader("referer") + "&msg=login-failed");
        }
    }

    private boolean isValidOauthGetRequest(HttpServletRequest req) {
        for (String para : requiredParamters) {
            if (req.getParameter(para) == null) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidOauthClient(HttpServletRequest req) {
        String clientId = req.getParameter(OAuth.OAUTH_CLIENT_ID);
        String redirectUrl = req.getParameter(OAuth.OAUTH_REDIRECT_URI);

        OauthClient oauthClient = oauthClientService.getOauthClient(clientId);

        return oauthClient != null && oauthClient.getRedirectUri().equals(redirectUrl);
    }


}
