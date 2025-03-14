package com.example.basket.common;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

public class Validation {
    private static final Validator validator = buildDefaultValidatorFactory().getValidator();

    public static <T> T requireValid(T value) {
        var violations = validator.validate(value);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return value;
    }
}
