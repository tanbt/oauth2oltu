package com.tanbt.oauth2oltu.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="oauth_access_tokens")
@Document(collection = "oauth_access_tokens")
public class OauthAccessToken {

    @Id
    @org.springframework.data.annotation.Id
    private int id;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "client_id")
    private String clientId;

    private String scope;

    @Column(columnDefinition="TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date expires;

    @Column(name = "user_id")
    private int userId;

    public OauthAccessToken() {
    }

    public OauthAccessToken(String accessToken, String clientId,
            String scope, Date expires, int userId) {
        this.accessToken = accessToken;
        this.clientId = clientId;
        this.scope = scope;
        this.expires = expires;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
