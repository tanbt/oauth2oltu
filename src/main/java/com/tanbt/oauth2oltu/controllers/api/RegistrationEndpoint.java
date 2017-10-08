package com.tanbt.oauth2oltu.controllers.api;

/**
 *       Copyright 2010 Newcastle University
 *
 *          http://research.ncl.ac.uk/smart/
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.ext.dynamicreg.server.request.JSONHttpServletRequestWrapper;
import org.apache.oltu.oauth2.ext.dynamicreg.server.request.OAuthServerRegistrationRequest;
import org.apache.oltu.oauth2.ext.dynamicreg.server.response.OAuthServerRegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tanbt.oauth2oltu.controllers.api.demo.ServerContent;
import com.tanbt.oauth2oltu.service.UserService;

@RestController
public class RegistrationEndpoint {

    @Autowired
    @Qualifier("userService")
    UserService userService;

    @RequestMapping(value = "/api/register", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    public String register(@Context HttpServletRequest request) throws
            OAuthSystemException {


        OAuthServerRegistrationRequest oauthRequest = null;
        try {
            oauthRequest = new OAuthServerRegistrationRequest(new JSONHttpServletRequestWrapper(request));
            oauthRequest.getType();
            oauthRequest.discover();
            oauthRequest.getClientName();
            oauthRequest.getClientUrl();
            oauthRequest.getClientDescription();
            oauthRequest.getRedirectURI();

            OAuthResponse response = OAuthServerRegistrationResponse
                .status(HttpServletResponse.SC_OK)
                .setClientId(ServerContent.CLIENT_ID)
                .setClientSecret(ServerContent.CLIENT_SECRET)
                .setIssuedAt(ServerContent.ISSUED_AT)
                .setExpiresIn(ServerContent.EXPIRES_IN)
                .buildJSONMessage();
            return response.getBody();

        } catch (OAuthProblemException e) {
            OAuthResponse response = OAuthServerRegistrationResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .error(e)
                .buildJSONMessage();
            return response.getBody();
        }

    }
}
