package com.tanbt.oauth2oltu.controllers;

import java.net.URISyntaxException;
import java.util.Date;

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
import com.tanbt.oauth2oltu.entity.OauthAccessToken;
import com.tanbt.oauth2oltu.entity.OauthAuthorizationCode;
import com.tanbt.oauth2oltu.entity.OauthClient;
import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.service.OauthAccessTokenService;
import com.tanbt.oauth2oltu.service.OauthAuthorizationCodeService;
import com.tanbt.oauth2oltu.service.OauthClientService;
import com.tanbt.oauth2oltu.service.OauthRefreshTokenService;
import com.tanbt.oauth2oltu.service.OauthScopeService;
import com.tanbt.oauth2oltu.service.UserService;
import com.tanbt.oauth2oltu.utils.OauthUtils;

import static com.tanbt.oauth2oltu.controllers.api.TokenApi.TOKEN_EXPIRE_DURATION;

@Controller
public class LoginController {

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @Autowired
    @Qualifier("oauthClientService")
    OauthClientService oauthClientService;

    @Autowired
    @Qualifier("oauthScopeService")
    OauthScopeService oauthScopeService;

    @Autowired
    @Qualifier("oauthAccessTokenService")
    OauthAccessTokenService oauthAccessTokenService;

    @Autowired
    @Qualifier("oauthAuthorizationCodeService")
    OauthAuthorizationCodeService oauthAuthorizationCodeService;

    @Autowired
    @Qualifier("oauthRefreshTokenService")
    OauthRefreshTokenService oauthRefreshTokenService;


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

        ModelAndView mav = new ModelAndView("empty");
        if (!isValidOauthGetRequest(request)) {
            mav.addObject("message", "This login page must be accessed from" +
                    " a client website for Oauth2.");
            return mav;
        }

        if (!isValidOauthClient(request)) {
            mav.addObject("message",
                    "Oauth Client user information is " + "wrong.");
            return mav;
        }

        String scope = request.getParameter(OAuth.OAUTH_SCOPE);
        if (oauthScopeService.getOauthScope(scope) == null) {
            mav.addObject("message", "Scope is incorrect.");
            return mav;
        }

        mav = new ModelAndView("login");
        mav.addObject("login", new Login());
        mav.addObject("parameters", request.getQueryString());
        String msg = request.getParameter("msg");
        if (msg != null) {
            mav.addObject("message", "Wrong username or password.");
        }
        return mav;

    }

    /**
     * loginProcess/?redirect_uri=http://localhost:8081/loginByOauth&scope=user:read&response_type=code&client_id=testid1234
     */
    @RequestMapping(value = "/oauth2/loginProcess", method = RequestMethod.POST)
    public RedirectView loginProcess(HttpServletRequest request,
            HttpServletResponse res, @ModelAttribute("login") Login login)
            throws URISyntaxException, OAuthSystemException {
        User user = userService.getUser(login.getEmail(), login.getPassword());

        if (null != user) {
            OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(
                    new MD5Generator());

            String clientId = request.getParameter(OAuth.OAUTH_CLIENT_ID);
            String scope = request.getParameter(OAuth.OAUTH_SCOPE);
            String responseType = request
                    .getParameter(OAuth.OAUTH_RESPONSE_TYPE);

            String token = oauthIssuerImpl.accessToken();
            String code = oauthIssuerImpl.authorizationCode();

            if (responseType.equals("code")) {
                OauthAuthorizationCode oauthAuthorizationCode = new OauthAuthorizationCode(
                        code, OauthUtils.getExpireDate(TOKEN_EXPIRE_DURATION),
                        scope, clientId, user.getId());
                oauthAuthorizationCodeService.save(oauthAuthorizationCode);
            } else {
                OauthAccessToken oauthAccessToken = new OauthAccessToken(token,
                        clientId, scope,
                        OauthUtils.getExpireDate(TOKEN_EXPIRE_DURATION),
                        user.getId());
                oauthAccessTokenService.save(oauthAccessToken);
            }

            return OauthUtils.redirect(
                    OauthUtils.GenerateLinkAfterLogin(request, code, token));
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

        return oauthClient != null &&
                oauthClient.getRedirectUri().equals(redirectUrl);
    }

}
