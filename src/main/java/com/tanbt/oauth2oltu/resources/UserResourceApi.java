package com.tanbt.oauth2oltu.resources;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tanbt.oauth2oltu.entity.OauthAccessToken;
import com.tanbt.oauth2oltu.entity.User;
import com.tanbt.oauth2oltu.service.OauthAccessTokenService;
import com.tanbt.oauth2oltu.service.UserService;
import com.tanbt.oauth2oltu.utils.OauthUtils;

@RestController
public class UserResourceApi {

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @Autowired
    @Qualifier("oauthAccessTokenService")
    OauthAccessTokenService oauthAccessTokenService;

    /**
     * This is an example how to fetch User Resource by Access Token
     * Only the info of the user belongs to the access token is accessible.
     *
     * You can develop more resources endpoint and rules to access to them.
     */
    @RequestMapping(value = "/oauth/user/{id}", method = RequestMethod.GET, produces
            = "application/json")
    public ResponseEntity getResourceByHeader(
            @Context HttpServletRequest request, @PathVariable("id") int id) {

        //TODO: read access token, get userid, get scope, check if scope is
        // user:read, and user id is correct
        Optional<OAuthAccessResourceRequest> oauthRequest = Optional.ofNullable(
                generateResourceRequest(request, ParameterStyle.HEADER));

        return processRequest(oauthRequest.get(), id);
    }

    @RequestMapping(value = "/oauth/user", method = RequestMethod.GET, produces
            = "application/json")
    public ResponseEntity getResourceByHeader(
            @Context HttpServletRequest request) {

        //TODO: read access token, get userid, get scope, check if scope is
        // user:read, and user id is correct
        Optional<OAuthAccessResourceRequest> oauthRequest = Optional.ofNullable(
                generateResourceRequest(request, ParameterStyle.HEADER));

        return processRequest(oauthRequest.get(), -1);
    }

    /**
     * @param oauthRequest
     * @param id -1 means the wanting user is based on Access Token
     * @return
     */
    private ResponseEntity<String> processRequest(
            OAuthAccessResourceRequest oauthRequest, int id){
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

        String responseData = getUserResource(accessToken, id);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    private String getUserResource(OauthAccessToken accessToken, int userId){
        if (userId != -1 && accessToken.getUserId() != userId) {
            return "{\"msg\": \"Don't have permission to get " +
                    "this data\"}";
        }
        User user = userService.findById(accessToken.getUserId());
        Gson gson = new Gson();

        JsonParser parser = new JsonParser();
        JsonObject userJsonObj = parser.parse(gson.toJson(user))
                .getAsJsonObject();
        userJsonObj.remove("password");
        return userJsonObj.toString();
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
