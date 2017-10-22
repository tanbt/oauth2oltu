package com.tanbt.oauth2oltu.service;


import org.springframework.beans.factory.annotation.Autowired;

import com.tanbt.oauth2oltu.entity.OauthAuthorizationCode;
import com.tanbt.oauth2oltu.repository.mysql.OauthAuthorizationCodeRepository;

public class OauthAuthorizationCodeService {

    @Autowired
    private OauthAuthorizationCodeRepository repo;

    public OauthAuthorizationCode save(OauthAuthorizationCode token) {
        return repo.saveAndFlush(token);
    }

    public OauthAuthorizationCode findByCode(String code) {
        try {
            return repo.findByCode(code).get(0);
        } catch (Exception ex) {
            return null;
        }
    }
}
