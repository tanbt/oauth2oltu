package com.tanbt.oauth2oltu.resources;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tanbt.oauth2oltu.entity.OauthAccessToken;
import com.tanbt.oauth2oltu.service.OauthAccessTokenService;
import com.tanbt.oauth2oltu.utils.OauthUtils;

@RestController
public class UserResourceApi {

    @Autowired
    @Qualifier("oauthAccessTokenService")
    OauthAccessTokenService oauthAccessTokenService;

    /**
     * Read user info of the user belongs to the access token
     */
    @RequestMapping(value = "/oauth/user/{id}", method = RequestMethod.GET, produces
            = "application/json")
    public ResponseEntity getResourceByHeader(
            @Context HttpServletRequest request, @PathVariable("id") int id) {

        //TODO: read access token, get userid, get scope, check if scope is
        // user:read, and user id is correct
        Optional<OAuthAccessResourceRequest> oauthRequest = Optional.ofNullable(
                generateResourceRequest(request, ParameterStyle.HEADER));

        return processRequest(oauthRequest.get());
    }

    private ResponseEntity<String> processRequest(
            OAuthAccessResourceRequest oauthRequest){
        // TODO: should have a method to find by token and date
        OauthAccessToken accessToken = null;
        try {
            accessToken = oauthAccessTokenService
                        .findByAccessToken(oauthRequest.getAccessToken());
        } catch (OAuthSystemException e) {
            return new ResponseEntity<>(
                    OauthUtils.convertToJsonMessage("msg", "Cannot get token."),
                    HttpStatus.BAD_REQUEST);
        }

        if (accessToken == null) {
            return new ResponseEntity<>(
                    OauthUtils.convertToJsonMessage("msg", "Cannot authorize."),
                    HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(
                OauthUtils.convertToJsonMessage("msg", "Token is valid."),
                HttpStatus.OK);
    }

    private OAuthAccessResourceRequest generateResourceRequest(
            HttpServletRequest request, ParameterStyle style) {
        try {
            return new OAuthAccessResourceRequest(request, style);
        } catch (Exception e) {
            return null;
        }
    }

}
