package com.jobmanagement.exception;

import java.util.List;

public class PasswordValidationException extends RuntimeException {
    private final List<String> validationErrors;

    public PasswordValidationException(List<String> validationErrors) {
        super("Şifre gereksinimleri karşılanmadı");
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
} 