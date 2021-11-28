package com.gbdevteam.teamnotes.controller.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomValidatorUUID.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {
    String message() default "UUID  not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
