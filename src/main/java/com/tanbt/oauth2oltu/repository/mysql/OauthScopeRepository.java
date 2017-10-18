package com.tanbt.oauth2oltu.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanbt.oauth2oltu.entity.OauthScope;

@Repository
public interface OauthScopeRepository
        extends JpaRepository<OauthScope, String> {

    List<OauthScope> findByScope(String name);
}
