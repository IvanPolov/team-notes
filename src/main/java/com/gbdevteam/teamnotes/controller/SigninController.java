package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.UserAuthDto;

import com.gbdevteam.teamnotes.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.validation.Valid;


@RestController
@SessionScope
@RequiredArgsConstructor
@Slf4j

@RequestMapping("/api/v1/login")
@Validated
public class SigninController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody UserAuthDto userAuthDto) {

        if (userService.login(userAuthDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                    .body(Problem.create()
                            .withTitle("Authentication error")
                            .withDetail("The email or password does not match."));
        }
    }

}
