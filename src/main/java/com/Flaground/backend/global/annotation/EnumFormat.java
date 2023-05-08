package com.Flaground.backend.global.annotation;

import com.Flaground.backend.global.annotation.validator.EnumFormatValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumFormatValidator.class)
public @interface EnumFormat {
    String message() default "해당 필드의 타입에서 지원하지 않는 값입니다.";

    Class[] groups() default {};

    /**
     * 어노테이션을 적용하는 Enum의 타입을 지정해준다.
     */
    Class<? extends Enum> enumClass();

    Class[] payload() default {};
}
