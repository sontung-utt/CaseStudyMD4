package com.codegym.casestudymd4.model.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password không được để trống!").addConstraintViolation();
            return false;
        }

        if (password.length() < 8 || password.length() > 20) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Password phải có độ dài từ 8 đến 20 ký tự!").addConstraintViolation();
            return false;
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            context.disableDefaultConstraintViolation();  // Tắt thông báo mặc định
            context.buildConstraintViolationWithTemplate("Password phải ít nhất một chữ thường, chữ hoa, chữ số và ký tự đặc biệt(@$!%*?&)")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
