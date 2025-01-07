package com.zbinyds.learn.validate.core.validator;

import com.zbinyds.learn.validate.core.annotation.NaturalNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * @Author zbinyds
 * @Create 2024-11-18 14:07
 */
public class NaturalNumberConstraintValidator implements ConstraintValidator<NaturalNumber, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return Optional.ofNullable(value).map(v -> v > 0).orElse(false);
    }
}
