package com.gbdevteam.teamnotes.controller.validators;

import java.lang.annotation.*;
import javax.validation.*;

@Documented
@Constraint(validatedBy = CustomValidatorUUID.class)
@Target( {ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {
    String message() default "{uuid.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
