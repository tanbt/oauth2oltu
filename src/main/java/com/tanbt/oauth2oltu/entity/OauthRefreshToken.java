package com.tanbt.oauth2oltu.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="oauth_refresh_tokens")
@Document(collection = "oauth_refresh_tokens")
public class OauthRefreshToken {

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "client_id")
    private String clientId;

    private String scope;

    @Column(columnDefinition="TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date expires;

    @Column(name = "user_id")
    private int userId;

    public OauthRefreshToken() {
    }

    public OauthRefreshToken(String accessToken, String clientId,
            String scope, Date expires, int userId) {
        this.refreshToken = accessToken;
        this.clientId = clientId;
        this.scope = scope;
        this.expires = expires;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getAccessToken() {
        return refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.refreshToken = accessToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
