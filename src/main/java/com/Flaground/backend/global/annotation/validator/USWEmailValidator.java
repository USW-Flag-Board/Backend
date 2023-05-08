package com.Flaground.backend.global.annotation.validator;

import com.Flaground.backend.global.annotation.USWEmailFormat;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class USWEmailValidator implements ConstraintValidator<USWEmailFormat, String> {
    private static final String USW_EMAIL = "@suwon.ac.kr";
    private static final int NOT_EMAIL = -1;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int separateIndex = getIndex(value);
        return isValidUSWEmail(value, separateIndex);
    }

    private int getIndex(String email) {
        return StringUtils.indexOf(email, "@");
    }

    private boolean isUSWEmailFormat(String email, int separateIndex) {
        return StringUtils.equals(email.substring(separateIndex), USW_EMAIL);
    }

    private boolean isEmailFormat(int index) {
        return index != NOT_EMAIL;
    }

    private boolean isValidUSWEmail(String value, int separateIndex) {
        return isEmailFormat(separateIndex) && isUSWEmailFormat(value, separateIndex);
    }
}
