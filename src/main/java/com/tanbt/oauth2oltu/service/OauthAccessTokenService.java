package com.tanbt.oauth2oltu.service;


import org.springframework.beans.factory.annotation.Autowired;

import com.tanbt.oauth2oltu.entity.OauthAccessToken;
import com.tanbt.oauth2oltu.repository.mysql.OauthAccessTokenRepository;

public class OauthAccessTokenService {

    @Autowired
    private OauthAccessTokenRepository repo;

    public OauthAccessToken save(OauthAccessToken token) {
        return repo.saveAndFlush(token);
    }


    public OauthAccessToken findByAccessToken(String token) {
        try {
            return repo.findByAccessToken(token).get(0);
        } catch (Exception ex) {
            return null;
        }
    }
}
