package com.tanbt.oauth2oltu.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanbt.oauth2oltu.entity.OauthAuthorizationCode;

@Repository
public interface OauthAuthorizationCodeRepository
        extends JpaRepository<OauthAuthorizationCode, String> {

    List<OauthAuthorizationCode> findByCode(String code);

}
