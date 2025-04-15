package com.jobmanagement.controller;

import com.jobmanagement.dto.UserRegistrationDto;
import com.jobmanagement.exception.PasswordValidationException;
import com.jobmanagement.model.User;
import com.jobmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            User user = userService.registerUser(registrationDto);
            return ResponseEntity.ok(user);
        } catch (PasswordValidationException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Şifre gereksinimleri karşılanmadı");
            response.put("validationErrors", e.getValidationErrors());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 