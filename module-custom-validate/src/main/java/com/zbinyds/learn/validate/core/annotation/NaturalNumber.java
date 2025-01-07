package com.zbinyds.learn.validate.core.annotation;

import com.zbinyds.learn.validate.core.validator.NaturalNumberConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author zbinyds
 * @Create 2024-11-18 14:00
 */

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {NaturalNumberConstraintValidator.class})
public @interface NaturalNumber {
    String message() default "{com.zbinyds.learn.validate.annotation.NaturalNumber.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
