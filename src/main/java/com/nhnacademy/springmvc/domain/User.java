package com.nhnacademy.springmvc.domain;

import lombok.Getter;

@Getter
public class User {
    public enum Auth{
        ROLE_ADMIN,ROLE_CUSTOMER
    }
    private String id;
    private String password;
    private String name;
    private Auth auth;
}
