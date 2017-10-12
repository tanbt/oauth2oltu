package com.tanbt.oauth2oltu.controllers.api;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tanbt.oauth2oltu.controllers.api.demo.TestContent;
import com.tanbt.oauth2oltu.service.UserService;

@RestController
public class TokenEndpoint {

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @RequestMapping(value = "/api/token", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity authorize(@Context HttpServletRequest request)
            throws OAuthSystemException {

        OAuthTokenRequest oauthRequest = null;

        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        try {
            oauthRequest = new OAuthTokenRequest(request);

            //check if client secret is valid
            if (!TestContent.CLIENT_SECRET
                    .equals(oauthRequest.getParam(OAuth.OAUTH_CLIENT_SECRET))) {
                OAuthResponse response = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription("client_secret invalid")
                        .buildJSONMessage();
                return new ResponseEntity(response.getBody(), HttpStatus.OK);
            }

            //check if clientid is valid
            if (!TestContent.CLIENT_ID
                    .equals(oauthRequest.getParam(OAuth.OAUTH_CLIENT_ID))) {
                OAuthResponse response = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription("client_id not found")
                        .buildJSONMessage();
                return new ResponseEntity(response.getBody(),
                        HttpStatus.UNAUTHORIZED);
            }

            //do checking for different grant types
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
                    .equals(GrantType.AUTHORIZATION_CODE.toString())) {
                if (!TestContent.AUTHORIZATION_CODE
                        .equals(oauthRequest.getParam(OAuth.OAUTH_CODE))) {
                    OAuthResponse response = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription("invalid authorization code")
                            .buildJSONMessage();
                    return new ResponseEntity(response.getBody(),
                            HttpStatus.UNAUTHORIZED);
                }
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
                    .equals(GrantType.PASSWORD.toString())) {
                if (!TestContent.PASSWORD.equals(oauthRequest.getPassword()) ||
                        !TestContent.USERNAME
                                .equals(oauthRequest.getUsername())) {
                    OAuthResponse response = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription("invalid username or password")
                            .buildJSONMessage();
                    return new ResponseEntity(response.getBody(),
                            HttpStatus.UNAUTHORIZED);
                }
            } else if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE)
                    .equals(GrantType.REFRESH_TOKEN.toString())) {
                if (!TestContent.REFRESH_TOKEN
                        .equals(oauthRequest.getRefreshToken())) {
                    OAuthResponse response = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription("invalid refresh token")
                            .buildJSONMessage();
                    return new ResponseEntity(response.getBody(),
                            HttpStatus.UNAUTHORIZED);
                }
            }

            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(oauthIssuerImpl.accessToken())
                    .setExpiresIn("3600")
                    .setRefreshToken(oauthIssuerImpl.refreshToken())
                    .buildJSONMessage();

            return new ResponseEntity(response.getBody(), HttpStatus.OK);
        } catch (OAuthProblemException e) {
            OAuthResponse res = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            return new ResponseEntity(res.getBody(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/api/token", method = RequestMethod.GET, consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity authorizeGet(@Context HttpServletRequest request)
            throws OAuthSystemException {
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        OAuthResponse response = OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setAccessToken(oauthIssuerImpl.accessToken())
                .setExpiresIn("3600").buildJSONMessage();

        return new ResponseEntity(response.getBody(), HttpStatus.OK);
    }

}
