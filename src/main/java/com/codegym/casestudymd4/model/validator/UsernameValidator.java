package com.codegym.casestudymd4.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Username không được để trống!").addConstraintViolation();
            return false;
        }

        if (username.length() < 6 || username.length() > 20) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Username phải có độ dài từ 6 đến 20 ký tự!").addConstraintViolation();
            return false;
        }

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Username chỉ được chứa chữ cái và số!").addConstraintViolation();
            return false;
        }

        return true;
    }
}
