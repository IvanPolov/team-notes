package com.gbdevteam.teamnotes.controller.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
@Slf4j
public final class ValidatorEmail {


    public static final String PATTERN_EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    public boolean matchByPattern(String source, String regexp) {
        log.info("Matcher use pattern: [" + regexp + "].");
        boolean resultMatch = java.util.regex.Pattern.compile(regexp)
                .matcher(source)
                .matches();
        log.info("Matcher say: " + resultMatch + "!  ");
        return resultMatch;
    }
}
