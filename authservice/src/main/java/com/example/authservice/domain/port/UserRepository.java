package com.example.authservice.domain.port;

import com.example.authservice.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    User save(User user);
}
