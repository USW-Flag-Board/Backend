package com.FlagHome.backend.global.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordFormatValidator implements ConstraintValidator<PasswordFormat, String> {
    private static final Pattern PASSWORD_PATTERN = Pattern
                        .compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        final Matcher matcher = PASSWORD_PATTERN.matcher(value);
        if (matcher.find()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
