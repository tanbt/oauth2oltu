package com.tanbt.oauth2oltu.controllers.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tanbt.oauth2oltu.controllers.api.demo.TestContent;

@RestController
public class ResourceHeaderEndpoint {

    @RequestMapping(value = "/api/resource_header", method = RequestMethod.GET, produces = "text/html")
    public ResponseEntity get(@Context HttpServletRequest request)
            throws OAuthSystemException {

        try {

            // Make the OAuth Request out of this request
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(
                    request, ParameterStyle.HEADER);

            // Get the access token
            String accessToken = oauthRequest.getAccessToken();

            // Validate the access token
            if (!TestContent.ACCESS_TOKEN_VALID.equals(accessToken)) {

                // Return the OAuth error message
                return new ResponseEntity(
                        OAuthError.ResourceResponse.INVALID_TOKEN,
                        HttpStatus.UNAUTHORIZED);

            }

            // Return the resource
            return new ResponseEntity("access_token_valid", HttpStatus.OK);

        } catch (OAuthProblemException e) {
            // Check if the error code has been set
            String errorCode = e.getError();
            if (OAuthUtils.isEmpty(errorCode)) {

                // Return the OAuth error message
                return new ResponseEntity("Cannot authorize.",
                        HttpStatus.UNAUTHORIZED);
            }

            return new ResponseEntity(e.getDescription(),
                    HttpStatus.BAD_REQUEST);
        }
    }

}


