package com.FlagHome.backend.global.annotation.validator;

import com.FlagHome.backend.global.annotation.PasswordFormat;
import org.apache.commons.lang3.StringUtils;

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
        return isNotNull(value) && matcher.matches();
    }

    private boolean isNotNull(String value) {
        return StringUtils.isNotBlank(value);
    }
}
