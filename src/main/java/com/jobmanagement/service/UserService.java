package com.jobmanagement.service;

import com.jobmanagement.dto.UserRegistrationDto;
import com.jobmanagement.model.Role;
import com.jobmanagement.model.User;
import com.jobmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserRegistrationDto registrationDto) {
        logger.info("Attempting to register user: {}", registrationDto.getUsername());
        
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            logger.warn("Username already exists: {}", registrationDto.getUsername());
            throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
        }

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            logger.warn("Email already exists: {}", registrationDto.getEmail());
            throw new RuntimeException("Bu e-posta adresi zaten kullanılıyor");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(registrationDto.getRole() != null ? registrationDto.getRole() : Role.WORKER);

        try {
            User savedUser = userRepository.save(user);
            logger.info("User registered successfully: {}", savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage());
            throw new RuntimeException("Kayıt işlemi sırasında bir hata oluştu");
        }
    }
} 