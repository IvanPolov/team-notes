package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.UserRegAuthDto;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@SessionScope
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Slf4j
@RequestMapping("/api/v1/signup")
public class SignupController {

    final UserService userService;

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> save(@Validated @RequestBody UserRegAuthDto userRegAuthDto, BindingResult result, Model model) throws Exception {

        if(result.hasErrors()){
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(errors, HttpStatus.OK);
        }else {
            if(userService.findByEmail(userRegAuthDto.getEmail()) != null){
                return new ResponseEntity<>(
                        Collections.singletonList("Email already exists"),
                        HttpStatus.CONFLICT);
            }else{
                User user = new User(userRegAuthDto);
                userService.create(user);
                auth(user);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }

    }
    @GetMapping
    public ResponseEntity<Object> validateEmail(@Validated @RequestParam String email){
            if (userService.findByEmail(email) != null) {
                return new ResponseEntity<>(
                        Collections.singletonList("Email already exists"),
                        HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
    }

    private void auth(User user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        Authentication authenticatedUser = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }
}
