package com.tanbt.oauth2oltu.controllers.api;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tanbt.oauth2oltu.controllers.api.demo.TestContent;

@RestController
public class ResourceBodyEndpoint {

    @RequestMapping(value = "/api/resource_body", method = RequestMethod.POST,
            consumes = "application/x-www-form-urlencoded", produces = "text/html")
    public ResponseEntity get(@Context HttpServletRequest request) throws
            OAuthSystemException {

        try {

            // Make the OAuth Request out of this request and validate it
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request,
                ParameterStyle.BODY);

            // Get the access token
            String accessToken = oauthRequest.getAccessToken();

            // Check if the token is valid
            if (TestContent.ACCESS_TOKEN_VALID.equals(accessToken)) {

                // Return the resource
                return new ResponseEntity("Access token is valid.",
                        HttpStatus.OK);
            }


            // Check if the token is not expired
            if (TestContent.ACCESS_TOKEN_EXPIRED.equals(accessToken)) {

                // Return the OAuth error message
                OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm(TestContent.RESOURCE_SERVER_NAME)
                    .setError(OAuthError.ResourceResponse.EXPIRED_TOKEN)
                    .buildHeaderMessage();

                // Return the error message
                return new ResponseEntity("Access token is expired.",
                        HttpStatus.UNAUTHORIZED);
            }


            // Check if the token is sufficient
            if (TestContent.ACCESS_TOKEN_INSUFFICIENT.equals(accessToken)) {

                // Return the OAuth error message
                OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_FORBIDDEN)
                    .setRealm(TestContent.RESOURCE_SERVER_NAME)
                    .setError(OAuthError.ResourceResponse.INSUFFICIENT_SCOPE)
                    .buildHeaderMessage();

                // Return the error message
                return new ResponseEntity("Access token is insufficient.",
                        HttpStatus.FORBIDDEN);
            }


            // Return the OAuth error message
            OAuthResponse oauthResponse = OAuthRSResponse
                .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setRealm(TestContent.RESOURCE_SERVER_NAME)
                .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                .buildHeaderMessage();

            //return Response.status(Response.Status.UNAUTHORIZED).build();
            return new ResponseEntity("Your request is unauthorized.",
                    HttpStatus.UNAUTHORIZED);

        } catch (OAuthProblemException e) {

            // Check if the error code has been set
            String errorCode = e.getError();
            if (OAuthUtils.isEmpty(errorCode)) {

                // Return the OAuth error message
                OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm(TestContent.RESOURCE_SERVER_NAME)
                    .buildHeaderMessage();

                // If no error code then return a standard 401 Unauthorized response
                return new ResponseEntity("Your request is unauthorized.",
                        HttpStatus.UNAUTHORIZED);
            }

            return new ResponseEntity(e.getDescription(),
                    HttpStatus.BAD_REQUEST);
        }
    }

}
