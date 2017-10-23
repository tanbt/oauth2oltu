package com.tanbt.oauth2oltu.controllers.api;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tanbt.oauth2oltu.entity.OauthAccessToken;
import com.tanbt.oauth2oltu.service.OauthAccessTokenService;
import com.tanbt.oauth2oltu.utils.OauthUtils;

@RestController
public class ResourceApi {

    @Autowired
    @Qualifier("oauthAccessTokenService")
    OauthAccessTokenService oauthAccessTokenService;

    /**
     POST /oauth/resource-body HTTP/1.1
     Content-Type: application/x-www-form-urlencoded
     access_token=8f4033b05c361aa5565c751ffe3eee28
     */
    @RequestMapping(value = "/oauth/resource-body", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity getResourceByForm(
            @Context HttpServletRequest request) {
        Optional<OAuthAccessResourceRequest> oauthRequest = Optional.ofNullable(
                generateResourceRequest(request, ParameterStyle.BODY));

        return processRequest(oauthRequest.get());
    }

    /**
     * GET /oauth/resource-header HTTP/1.1 Host:
     * localhost:8080 Authorization: Bearer 8f4033b05c361aa5565c751ffe3eee28
     */
    @RequestMapping(value = "/oauth/resource-header", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity getResourceByHeader(
            @Context HttpServletRequest request) {

        Optional<OAuthAccessResourceRequest> oauthRequest = Optional.ofNullable(
                generateResourceRequest(request, ParameterStyle.HEADER));

        return processRequest(oauthRequest.get());
    }

    /**
     * You can define more rules for the request's parameters and return
     * specific resources
     *
     * http://localhost:8080/oauth/resource-query?access_token=access_token_valid
     */
    @RequestMapping(value = "/oauth/resource-query", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getResource(
            @Context HttpServletRequest request)
            throws OAuthProblemException, OAuthSystemException {

        Optional<OAuthAccessResourceRequest> oauthRequest = Optional.ofNullable(
                generateResourceRequest(request, ParameterStyle.QUERY));

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
