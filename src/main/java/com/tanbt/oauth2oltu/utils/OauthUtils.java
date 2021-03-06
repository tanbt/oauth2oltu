package com.tanbt.oauth2oltu.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.tanbt.oauth2oltu.controllers.LoginController;
import com.tanbt.oauth2oltu.controllers.api.TokenApi;

public class OauthUtils {

    public static URI GenerateLinkAfterLogin(HttpServletRequest request,
            String code, String token)
            throws OAuthSystemException, URISyntaxException {

        OAuthAuthzRequest oauthRequest = null;
        try {
            oauthRequest = new OAuthAuthzRequest(request);

            String responseType = oauthRequest
                    .getParam(OAuth.OAUTH_RESPONSE_TYPE);

            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                    .authorizationResponse(request,
                            HttpServletResponse.SC_FOUND);

            if (responseType.equals(ResponseType.CODE.toString())) {
                builder.setCode(code);
                builder.setExpiresIn(TokenApi.TOKEN_EXPIRE_DURATION);
            }
            if (responseType.equals(ResponseType.TOKEN.toString())) {
                builder.setAccessToken(token);
                builder.setExpiresIn(TokenApi.TOKEN_EXPIRE_DURATION);
            }

            String redirectURI = oauthRequest
                    .getParam(OAuth.OAUTH_REDIRECT_URI);

            final OAuthResponse response = builder.location(redirectURI)
                    .buildQueryMessage();
            return new URI(response.getLocationUri());
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
            return new URI(response.getLocationUri());
        }
    }

    public static RedirectView redirect(URI url) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url.toString());
        return redirectView;
    }

    public static RedirectView redirect(String url) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }

    public static Date getExpireDate(Long duration) {
        Date today = new Date();
        return new Date(today.getTime() + duration * 1000);
    }

    public static String convertToJsonMessage(String key, String value) {
        return String.format("{\"%s\":\"%s\"}", key, value);
    }

    /**
     * Check if today is later than a given day
     *  which is usually an expired day
     *
     * @param expiredDay
     * @return true if today is later than expired day
     */
    public static boolean isLaterThan(Date expiredDay) {
        Date today = new Date();
        return today.after(expiredDay);
    }
}
