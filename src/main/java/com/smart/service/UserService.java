package com.smart.service;

import com.smart.entities.User;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserService {
    public Optional<User> findByEmail(String email);

    public User getUserByUsername(String email);

    public Optional<User> findById(Integer id);

    public void delete(User user);

    public void save(User user);

}
