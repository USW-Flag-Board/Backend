package com.FlagHome.backend.global.annotation;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class USWEmailValidator implements ConstraintValidator<USWEmailFormat, String> {
    private static final String USW_EMAIL = "@suwon.ac.kr";
    private static final int NOT_EMAIL = -1;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int separateIndex = getValidIndex(value);
        if (isUSWEmail(value, separateIndex)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean isUSWEmail(String email, int separateIndex) {
        return StringUtils.equals(email.substring(separateIndex), USW_EMAIL);
    }
    
    private int getValidIndex(String email) {
        int separateIndex = StringUtils.indexOf(email, "@");
        validateEmail(separateIndex);
        return separateIndex;
    }

    private void validateEmail(int index) {
        if (index == NOT_EMAIL) {
            throw new CustomException(ErrorCode.NOT_EMAIL);
        }
    }
}
