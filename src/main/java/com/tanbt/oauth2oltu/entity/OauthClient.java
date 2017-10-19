package com.tanbt.oauth2oltu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Table(name="oauth_clients")
@Document(collection = "oauth_clients")
public class OauthClient {

    @Id
    @org.springframework.data.annotation.Id

    @Column(name = "client_id")
    private String id;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "grant_types")
    private String grantTypes;

    @Column(name = "client_uri")
    private String clientUri;

    @Column(name = "redirect_uri")
    private String redirectUri;

    @Column(name = "user_id")
    private int userId;

    public OauthClient() {
    }

    public OauthClient(String id, String clientSecret, String appName,
            String grantTypes, String clientUri, String redirectUri,
            int userId) {
        this.id = id;
        this.clientSecret = clientSecret;
        this.appName = appName;
        this.grantTypes = grantTypes;
        this.clientUri = clientUri;
        this.redirectUri = redirectUri;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getClientUri() {
        return clientUri;
    }

    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "OauthClient{" + "id='" + id + '\'' + ", clientSecret='" +
                clientSecret + '\'' + ", appName='" + appName + '\'' +
                ", grantTypes='" + grantTypes + '\'' + ", clientUri='" +
                clientUri + '\'' + ", redirectUri='" + redirectUri + '\'' +
                ", userId=" + userId + '}';
    }
}
