package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.User;
import java.util.HashMap;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository{
    private final Map<String, User> userMap = new HashMap<>();

    @Override
    public boolean exists(String id) {
        return false;
    }

    @Override
    public boolean matches(String id, String password) {
        return false;
    }

    @Override
    public User getUser(String id) {
        return null;
    }

    @Override
    public User addUser(String id, String password) {
        return null;
    }
}
