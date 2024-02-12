package com.smart.service;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User getUserByUsername(String email) {
        return this.userRepository.getUserByUsername(email);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return this.userRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(User user) {
        this.userRepository.delete(user);
    }

    @Override
    public void save(User user) {
        this.userRepository.save(user);
    }
}
