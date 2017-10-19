package com.tanbt.oauth2oltu.service;


import org.springframework.beans.factory.annotation.Autowired;

import com.tanbt.oauth2oltu.entity.OauthClient;
import com.tanbt.oauth2oltu.repository.mysql.OauthClientRepository;

public class OauthClientService {

    @Autowired
    private OauthClientRepository repo;

    public OauthClient getOauthClient(String id) {
        try {
            return repo.findById(id).get(0);
        } catch (Exception ex) {
            return null;
        }
    }

    public OauthClient save(OauthClient client) {
        return repo.saveAndFlush(client);
    }
}
