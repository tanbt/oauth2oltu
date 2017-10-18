package com.tanbt.oauth2oltu.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanbt.oauth2oltu.entity.OauthClient;

@Repository
public interface OauthClientRepository
        extends JpaRepository<OauthClient, String> {

    List<OauthClient> findById(String id);
}
