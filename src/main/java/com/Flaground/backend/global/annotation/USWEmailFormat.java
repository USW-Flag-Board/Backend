package com.Flaground.backend.global.annotation;

import com.Flaground.backend.global.annotation.validator.USWEmailValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = USWEmailValidator.class)
public @interface USWEmailFormat {
    String message() default "수원대학교 웹 메일 주소가 아닙니다.";

    Class[] groups() default {};

    Class[] payload() default {};
}
