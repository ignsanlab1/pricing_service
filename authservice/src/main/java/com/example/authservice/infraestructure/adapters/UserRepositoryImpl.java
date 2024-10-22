package com.example.authservice.infraestructure.adapters;

import com.example.authservice.domain.model.User;
import com.example.authservice.domain.port.UserRepository;
import com.example.authservice.infraestructure.repository.UserJpaRepository;
import com.example.authservice.infraestructure.rest.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper; // Asumiendo que tienes un mapper para User y UserEntity

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(userMapper::toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userMapper::toUser);
    }

    @Override
    public User save(User user) {
        return Optional.ofNullable(user)
                .map(userMapper::toUserEntity)
                .map(userJpaRepository::save)
                .map(userMapper::toUser)
                .orElseThrow(() -> new RuntimeException("Error saving the user"));

    }
}
