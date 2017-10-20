package com.tanbt.oauth2oltu.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Table(name="oauth_scopes")
@Document(collection = "oauth_scopes")
public class OauthScope {

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String scope;

    private String description;

    public OauthScope() {
    }

    public OauthScope(int id, String scope, String description) {
        this.id = id;
        this.scope = scope;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
