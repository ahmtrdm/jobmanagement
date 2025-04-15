package com.jobmanagement.service;

import com.jobmanagement.dto.UserRegistrationDto;
import com.jobmanagement.exception.PasswordValidationException;
import com.jobmanagement.model.EmployerProfile;
import com.jobmanagement.model.Role;
import com.jobmanagement.model.User;
import com.jobmanagement.repository.EmployerProfileRepository;
import com.jobmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployerProfileRepository employerProfileRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmployerProfileRepository employerProfileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.employerProfileRepository = employerProfileRepository;
    }

    private void validatePassword(String password) {
        List<String> errors = new ArrayList<>();

        if (password.length() < 6) {
            errors.add("Şifre en az 6 karakter olmalıdır");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Şifre en az 1 büyük harf içermelidir");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.add("Şifre en az 1 küçük harf içermelidir");
        }
        if (!password.matches(".*[0-9].*")) {
            errors.add("Şifre en az 1 rakam içermelidir");
        }

        if (!errors.isEmpty()) {
            throw new PasswordValidationException(errors);
        }
    }

    @Transactional
    public User registerUser(UserRegistrationDto registrationDto) {
        logger.info("Attempting to register user: {}", registrationDto.getUsername());
        
        validatePassword(registrationDto.getPassword());

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
            if (savedUser.getRole().name().equals("EMPLOYER")) {
                EmployerProfile profile = new EmployerProfile();
                profile.setUsername(savedUser.getUsername());
                profile.setEmail(savedUser.getEmail());
                employerProfileRepository.save(profile);
            }
            return savedUser;
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage());
            throw new RuntimeException("Kayıt işlemi sırasında bir hata oluştu");
        }
    }
} 