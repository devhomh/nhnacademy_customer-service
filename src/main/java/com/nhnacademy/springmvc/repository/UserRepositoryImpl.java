package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.User;
import com.nhnacademy.springmvc.exception.UserAlreadyExistsException;
import com.nhnacademy.springmvc.exception.UserNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserRepositoryImpl implements UserRepository{
    private final Map<String, User> userMap = new HashMap<>();

    @Override
    public boolean exists(String id) {
        if(Objects.isNull(id)){
            throw new NullPointerException("Object is null");
        }

        return userMap.containsKey(id);
    }

    @Override
    public User getUser(String id) {
        if(Objects.isNull(id)){
            throw new NullPointerException("Object is null");
        }

        if(!exists(id)){
            throw new UserNotFoundException();
        }
        return userMap.get(id);
    }

    @Override
    public User addUser(User user) {
        if(Objects.isNull(user)){
            throw new NullPointerException("Object is null");
        }

        if (exists(user.getId())) {
            throw new UserAlreadyExistsException();
        }
        userMap.put(user.getId(), user);
        return userMap.get(user.getId());
    }

    @Override
    public boolean matches(String id, String password) {
        if(Objects.isNull(id) || Objects.isNull(password)){
            throw new NullPointerException("Object is null");
        }

        if(!exists(id)){
            throw new UserNotFoundException();
        }

        User user = getUser(id);
        return user.getPassword().equals(password);
    }
}
