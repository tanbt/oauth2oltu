package com.tanbt.oauth2oltu.service;


import java.util.List;

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

    public OauthAuthorizationCode findByUserId(int userId) {
        try {
            return repo.findByUserId(userId).get(0);
        } catch (Exception ex) {
            return null;
        }
    }

    public OauthAuthorizationCode findByClientIdAndUserId(String clientId,
            int userId) {
        try {
            return repo.findByClientIdAndUserId(clientId, userId).get(0);
        } catch (Exception ex) {
            return null;
        }
    }

    public void delete(OauthAuthorizationCode code) {
        repo.delete(code);
    }
}
