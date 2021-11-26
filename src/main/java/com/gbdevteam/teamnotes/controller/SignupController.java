package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.controller.util.ValidatorEmail;
import com.gbdevteam.teamnotes.dto.UserRegAuthDto;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;



@RestController
@SessionScope
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Slf4j

@RequestMapping("/api/v1/signup")
@Validated
public class SignupController {
    private final UserService userService;
    private final ValidatorEmail validatorEmail;

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Object> save(@Valid @RequestBody UserRegAuthDto userRegAuthDto, BindingResult result) {

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
                return new ResponseEntity<>(userService.addNewUser(userRegAuthDto), HttpStatus.CREATED);
            }
        }
    }

    @GetMapping
    public ResponseEntity<Object> validateEmail(
            @Pattern(regexp = ValidatorEmail.PATTERN_EMAIL)
            @RequestParam("email")
            @NotBlank
                    String email) {
        log.info(email);
        if (userService.findByEmail(email) != null ) {
            log.info(email + " CONFLICT!" + " Email already exists");
            return new ResponseEntity<>(
                    Collections.singletonList("Email already exists"),
                    HttpStatus.CONFLICT);
        } else {
            log.info(email + " is OK!");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
