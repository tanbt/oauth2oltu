package com.tanbt.oauth2oltu.service;


import org.springframework.beans.factory.annotation.Autowired;

import com.tanbt.oauth2oltu.entity.OauthScope;
import com.tanbt.oauth2oltu.repository.mysql.OauthScopeRepository;

public class OauthScopeService {

    @Autowired
    private OauthScopeRepository repo;

    public OauthScope getOauthScope(String name) {
        try {
            return repo.findByScope(name).get(0);
        } catch (Exception ex) {
            return null;
        }

    }
}
