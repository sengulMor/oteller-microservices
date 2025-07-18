package com.oteller.hotelservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConditionalNotBlankValidator.class)
@Documented
public @interface ConditionalNotBlank {

    String message() default  "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

