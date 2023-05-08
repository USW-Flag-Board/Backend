package com.Flaground.backend.global.annotation;

import com.Flaground.backend.global.annotation.validator.PasswordFormatValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordFormatValidator.class)
public @interface PasswordFormat {
    String message() default "사용할 수 없는 비밀번호 입니다. (8~20자 이내 영문, 숫자, 특수문자를 모두 포함)";

    Class[] groups() default {};

    Class[] payload() default {};
}
