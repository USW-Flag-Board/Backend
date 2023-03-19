package com.FlagHome.backend.global.annotation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class USWEmailValidator implements ConstraintValidator<USWEmailFormat, String> {
    private static final String USW_EMAIL = "@suwon.ac.kr";
    private static final int NOT_EMAIL = -1;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int separateIndex = getIndex(value);
        if (isEmailFormat(separateIndex) && isUSWEmail(value, separateIndex)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private int getIndex(String email) {
        return StringUtils.indexOf(email, "@");
    }

    private boolean isUSWEmail(String email, int separateIndex) {
        return StringUtils.equals(email.substring(separateIndex), USW_EMAIL);
    }

    private boolean isEmailFormat(int index) {
        return index != NOT_EMAIL;
    }
}
