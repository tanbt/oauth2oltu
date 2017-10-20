package com.tanbt.oauth2oltu.entity;

import javax.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * https://docs.jboss.org/hibernate/stable/annotations/reference/en/html/entity.html
 */

@Entity
@Table(name="users")
@Document(collection = "users")
public class User {

    @javax.persistence.Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String email;
    private String password;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;
    private int organization;

    public User() {
    }

    public User(int id, String email, String password, String firstname,
            String lastname, int organization) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.organization = organization;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getOrganization() {
        return organization;
    }

    public void setOrganization(int organization) {
        this.organization = organization;
    }
}
