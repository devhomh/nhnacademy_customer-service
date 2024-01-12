package com.nhnacademy.springmvc.domain;

import lombok.Getter;

@Getter
public class User {
    public enum Auth{
        ROLE_ADMIN,ROLE_CUSTOMER
    }
    private final String id;
    private final String password;
    private String name;
    private Auth auth;

    private User(String id, String password){
        this.id = id;
        this.password = password;
    }

    public static User create(String id, String password) {
        return new User(id, password);
    }

    public User setName(String name){
        this.name = name;
        return this;
    }

    public User setAuth(Auth auth){
        this.auth = auth;
        return this;
    }
}
