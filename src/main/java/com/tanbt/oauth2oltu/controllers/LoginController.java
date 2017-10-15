package com.tanbt.oauth2oltu.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
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

import static com.tanbt.oauth2oltu.utils.OauthUtils.redirect;

@Controller
public class LoginController {

    private String[] requiredParamters = new String[]{ OAuth
            .OAUTH_REDIRECT_URI, OAuth.OAUTH_SCOPE, OAuth
            .OAUTH_RESPONSE_TYPE, OAuth.OAUTH_CLIENT_ID};

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
            HttpServletResponse res,
            @ModelAttribute("login") Login login)
            throws OAuthSystemException, URISyntaxException {
        ModelAndView mav = null;
        User user = userService.getUser(login.getEmail(), login.getPassword());
        if (null != user) {

            // todo: refactoring to separate method or library
            OAuthAuthzRequest oauthRequest = null;

            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(
                    new MD5Generator());

            try {
                oauthRequest = new OAuthAuthzRequest(request);

                String responseType = oauthRequest
                        .getParam(OAuth.OAUTH_RESPONSE_TYPE);

                OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                        .authorizationResponse(request,
                                HttpServletResponse.SC_FOUND);

                if (responseType.equals(ResponseType.CODE.toString())) {
                    builder.setCode(oauthIssuerImpl.authorizationCode());
                    builder.setExpiresIn(3600l);
                }
                if (responseType.equals(ResponseType.TOKEN.toString())) {
                    builder.setAccessToken(oauthIssuerImpl.accessToken());
                    builder.setExpiresIn(3600l);
                }

                String redirectURI = oauthRequest
                        .getParam(OAuth.OAUTH_REDIRECT_URI);

                final OAuthResponse response = builder.location(redirectURI)
                        .buildQueryMessage();
                URI url = new URI(response.getLocationUri());
                return redirect(url);
            } catch (OAuthProblemException e) {

                final Response.ResponseBuilder responseBuilder = Response
                        .status(HttpServletResponse.SC_FOUND);

                String redirectUri = e.getRedirectUri();

                if (OAuthUtils.isEmpty(redirectUri)) {
                    throw new WebApplicationException(responseBuilder
                            .entity("OAuth callback url needs to be provided by client!!!")
                            .build());
                }
                final OAuthResponse response = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_FOUND).error(e)
                        .location(redirectUri).buildQueryMessage();
                final URI url = new URI(response.getLocationUri());
                return redirect(url);
            }
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
