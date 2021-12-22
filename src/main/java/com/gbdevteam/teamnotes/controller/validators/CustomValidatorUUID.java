package com.gbdevteam.teamnotes.controller.validators;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@NoArgsConstructor
@Slf4j
public class CustomValidatorUUID implements ConstraintValidator<ValidUUID, UUID> {

    public static final String PATTERN_UUID = "\b[0-9a-f]{8}\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\b[0-9a-f]{12}\b";

    @Override
    public void initialize(ValidUUID validUUID) {
    }

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext ctx) {
        log.info("Matcher use pattern: [" + PATTERN_UUID + "].");
        if (java.util.regex.Pattern.compile(PATTERN_UUID)
                .matcher(uuid.toString())
                .matches()) {
            ctx.disableDefaultConstraintViolation();
//            ctx.buildConstraintViolationWithTemplate(
//                            "{UUID is not Valid}")
//                    .addPropertyNode("UUID").addConstraintViolation();
            return false;
        }
        return true;
    }
}
