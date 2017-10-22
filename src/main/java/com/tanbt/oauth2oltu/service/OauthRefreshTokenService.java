package com.tanbt.oauth2oltu.service;


import org.springframework.beans.factory.annotation.Autowired;

import com.tanbt.oauth2oltu.entity.OauthRefreshToken;
import com.tanbt.oauth2oltu.repository.mysql.OauthRefreshTokenRepository;

public class OauthRefreshTokenService {

    @Autowired
    private OauthRefreshTokenRepository repo;

    public OauthRefreshToken save(OauthRefreshToken token) {
        return repo.saveAndFlush(token);
    }

    public OauthRefreshToken findByCode(String code) {
        try {
            return repo.findByRefreshToken(code).get(0);
        } catch (Exception ex) {
            return null;
        }
    }
}
