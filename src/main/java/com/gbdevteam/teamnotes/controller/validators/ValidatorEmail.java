//package com.gbdevteam.teamnotes.controller.validators;
//
//import com.gbdevteam.teamnotes.exception.UserEmailFoundException;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//
//@Component
//@NoArgsConstructor
//@Slf4j
//public final class ValidatorEmail {
//
//
//    public static final String PATTERN_EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
//
//    public boolean matchByPattern(String source, String regexp) {
//        log.info("Matcher use pattern: [" + regexp + "].");
//        boolean resultMatch = java.util.regex.Pattern.matches(regexp, source)                ;
//        log.info("Matcher say: " + resultMatch + "!  ");
////        if (!resultMatch) throw new UserEmailFoundException();
//        return true;
//    }
//}
