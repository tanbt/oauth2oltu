package com.tanbt.oauth2oltu.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanbt.oauth2oltu.entity.OauthRefreshToken;

@Repository
public interface OauthRefreshTokenRepository
        extends JpaRepository<OauthRefreshToken, String> {

}
