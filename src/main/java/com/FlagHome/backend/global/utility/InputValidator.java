package com.FlagHome.backend.global.utility;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class InputValidator {

    public void validateUSWEmail(String email) {
        // @가 포함되는 형식이 아니라면 -1 리턴
        int separateIndex = StringUtils.indexOf(email, "@");

        if (separateIndex == -1) {
            throw new CustomException(ErrorCode.NOT_EMAIL);
        }

        if (!StringUtils.equals(email.substring(separateIndex), "@suwon.ac.kr")) {
            throw new CustomException(ErrorCode.NOT_USW_EMAIL);
        }
    }

    public void validatePassword(String password) {
        Pattern passwordPattern = Pattern
                .compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$");

        Matcher matcher = passwordPattern.matcher(password);

        if (!matcher.find()) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public void validateCertification(String inputCertification, String savedCertification) {
        if (!StringUtils.equals(inputCertification, savedCertification)) {
            throw new CustomException(ErrorCode.CERTIFICATION_NOT_MATCH);
        }
    }
}
