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

import com.tanbt.oauth2oltu.entity.OauthAccessToken;
import com.tanbt.oauth2oltu.entity.OauthAuthorizationCode;
import com.tanbt.oauth2oltu.entity.OauthClient;
import com.tanbt.oauth2oltu.entity.OauthRefreshToken;
import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.service.OauthAccessTokenService;
import com.tanbt.oauth2oltu.service.OauthAuthorizationCodeService;
import com.tanbt.oauth2oltu.service.OauthClientService;
import com.tanbt.oauth2oltu.service.OauthRefreshTokenService;
import com.tanbt.oauth2oltu.service.UserService;
import com.tanbt.oauth2oltu.utils.OauthUtils;

@RestController
public class TokenApi {

    /**
     * Default expiration of the code is the seconds of a week
     */
    public static Long TOKEN_EXPIRE_DURATION = 604800l;
    public static Long REFRESH_EXPIRE_DURATION = TOKEN_EXPIRE_DURATION * 2;

    @Autowired
    @Qualifier("oauthAuthorizationCodeService")
    OauthAuthorizationCodeService oauthAuthorizationCodeService;

    @Autowired
    @Qualifier("oauthClientService")
    OauthClientService oauthClientService;

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @Autowired
    @Qualifier("oauthRefreshTokenService")
    OauthRefreshTokenService oauthRefreshTokenService;

    @Autowired
    @Qualifier("oauthAccessTokenService")
    OauthAccessTokenService oauthAccessTokenService;

    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Object> authorize(@Context HttpServletRequest request)
            throws OAuthSystemException {

        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

        String oauthCode = request.getParameter("code");
        String clientId = request.getParameter(OAuth.OAUTH_CLIENT_ID);
        String clientSecret = request.getParameter(OAuth.OAUTH_CLIENT_SECRET);
        String redirectUri = request.getParameter(OAuth.OAUTH_REDIRECT_URI);

        OAuthTokenRequest oauthRequest;

        try {
            oauthRequest = new OAuthTokenRequest(request);
            OauthClient oauthClient = oauthClientService
                    .getOauthClient(clientId);

            if (oauthClient == null) {
                return new ResponseEntity<Object>(generateErrorReponse(
                        OAuthError.TokenResponse.INVALID_CLIENT,
                        "Invalid client id").getBody(), HttpStatus.UNAUTHORIZED);
            }

            if (!oauthClient.getClientSecret().equals(clientSecret)) {
                return new ResponseEntity<Object>(generateErrorReponse(
                        OAuthError.TokenResponse.INVALID_CLIENT,
                        "Invalid client secret").getBody(), HttpStatus
                        .UNAUTHORIZED);
            }

            if (!oauthClient.getRedirectUri().equals(redirectUri)) {
                return new ResponseEntity<Object>(generateErrorReponse(
                        OAuthError.TokenResponse.INVALID_CLIENT,
                        "Invalid redirect url").getBody(), HttpStatus
                        .UNAUTHORIZED);
            }

            // process based on grant type
            String grantType = request.getParameter(OAuth.OAUTH_GRANT_TYPE);
            OauthAuthorizationCode oauthCodeObj = oauthAuthorizationCodeService
                    .findByCode(oauthCode);

            if (grantType.equals(GrantType.AUTHORIZATION_CODE.toString())) {
                if (oauthCodeObj == null) {
                    //todo: check expired code - easy
                    return new ResponseEntity<Object>(generateErrorReponse(
                            OAuthError.TokenResponse.INVALID_CLIENT,
                            "Invalid Authorization code ").getBody(),
                            HttpStatus.UNAUTHORIZED);
                }
            } else if (grantType.equals(GrantType.PASSWORD.toString())) {
                User endUser = userService.getUser(oauthRequest.getUsername(),
                        oauthRequest.getPassword());
                if (endUser == null) {
                    return new ResponseEntity<Object>(generateErrorReponse(
                            OAuthError.TokenResponse.INVALID_GRANT,
                            "Invalid user ").getBody(), HttpStatus.UNAUTHORIZED);
                }
            } else if (grantType.equals(GrantType.REFRESH_TOKEN.toString())) {
                OauthRefreshToken refreshToken = oauthRefreshTokenService
                        .findByCode(oauthRequest.getRefreshToken());

                if (refreshToken == null) {
                    return new ResponseEntity<Object>(generateErrorReponse(
                            OAuthError.TokenResponse.INVALID_GRANT,
                            "Invalid refresh token ").getBody(), HttpStatus
                            .UNAUTHORIZED);
                }
            }

            String accessToken = oauthIssuerImpl.accessToken();
            String refreshToken = oauthIssuerImpl.refreshToken();

            OauthAccessToken oauthAccessToken = new OauthAccessToken(
                    accessToken, clientId, oauthCodeObj.getScope(),
                    OauthUtils.getExpireDate(TOKEN_EXPIRE_DURATION),
                    oauthCodeObj.getUserId());
            oauthAccessTokenService.save(oauthAccessToken);

            OauthRefreshToken oauthRefreshToken = new OauthRefreshToken
                    (refreshToken, clientId, oauthCodeObj.getScope(),
                            OauthUtils.getExpireDate(REFRESH_EXPIRE_DURATION),
                            oauthCodeObj.getUserId());
            oauthRefreshTokenService.save(oauthRefreshToken);

            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.format("%s", TOKEN_EXPIRE_DURATION))
                    .setRefreshToken(refreshToken)
                    .buildJSONMessage();

            return new ResponseEntity<Object>(response.getBody(),
                    HttpStatus.OK);
        } catch (OAuthProblemException e) {
            OAuthResponse res = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            return new ResponseEntity<Object>(res.getBody(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private OAuthResponse generateErrorReponse(String responseCode, String desc)
            throws OAuthSystemException {
        return OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(responseCode).setErrorDescription(desc)
                .buildJSONMessage();
    }

}
