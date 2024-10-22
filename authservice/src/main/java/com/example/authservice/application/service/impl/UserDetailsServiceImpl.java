package com.example.authservice.application.service.impl;

import com.example.authservice.domain.port.UserRepository;
import com.example.authservice.infraestructure.repository.UserJpaRepository;
import com.example.authservice.infraestructure.rest.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("UserDetailsService user not found"));
    }
}
