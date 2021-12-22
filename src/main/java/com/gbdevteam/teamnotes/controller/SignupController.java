package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.controller.validators.*;
import com.gbdevteam.teamnotes.dto.UserRegAuthDto;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.mail.MessagingException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@SessionScope
@RequiredArgsConstructor
@Slf4j

@RequestMapping("/api/v1/signup")
@Validated
public class SignupController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody UserRegAuthDto userRegAuthDto, BindingResult result) throws MessagingException, UnsupportedEncodingException {

        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        } else {

            if (userService.findByEmail(userRegAuthDto.getEmail()) != null) {

                return new ResponseEntity<>(
                        Collections.singletonList("Email already exists"),
                        HttpStatus.CONFLICT);
            } else {
                userService.addNewUser(userRegAuthDto);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                        .body(Problem.create()
                                .withTitle("Email Sent")
                                .withDetail("We have sent an email to your email address to confirm your registration. \n" +
                                        "        Please check your email and verify your account within 30 days, otherwise your account will be deleted."));
            }
        }
    }

    @GetMapping
    public ResponseEntity<Object> validateEmail(@RequestParam("email") String email) {
        log.info(email);
        StringBuilder stringBuilder = new StringBuilder();

        HttpStatus httpStatus = HttpStatus.OK;

        if (!EmailValidator.getInstance().isValid(email)) {
            stringBuilder.append("Email: ").append(email).append(" - is not correct. ");
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (userService.findByEmail(email) != null) {
            stringBuilder.append("User with email: ").append(email).append(" already signup on service. ");
            httpStatus = HttpStatus.CONFLICT;
        }

        String resultError = stringBuilder.toString();
        if (httpStatus != HttpStatus.OK) {
            return ResponseEntity.status(httpStatus)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Email error")
                            .withDetail(resultError));
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

    @GetMapping("/confirm/")
    public ResponseEntity<String> confirmNewUserEmail(
            @RequestParam("email")
                    String email,
            @RequestParam("uuId")
            @ValidUUID
                    UUID uuId) {
        if (userService.verifyNewUserEmail(email, uuId)) {
            log.info("New User was confirmed by email!");
            return ResponseEntity.status(HttpStatus.OK)
                    .body("<div style='width:100%;height:90vh; display:flex; align-items:center;justify-content:center'><h3>Thank you! Your account was confirmed by email!</h3></div>");

        } else {
            log.error("Bad link to verify new user!");
            return ResponseEntity.badRequest()
                    .body("<div style='width:100%;height:90vh; display:flex; align-items:center;justify-content:center'><h3>Whoops! Your Link is not valid.</h3></div>");


        }
    }


}
