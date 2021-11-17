package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.UUID;

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
    public ResponseEntity<?> save(@Validated @RequestBody UserRegAuthDto userRegAuthDto) throws Exception {
        log.info(userRegAuthDto.getEmail());
        if (userService.findByEmail(userRegAuthDto.getEmail()) != null) {
            throw new Exception("Email all ready exist");//TODO handle and send message about this error on front
        }
        User user = new User(userRegAuthDto);
        userService.create(user);
        auth(user);
        return ResponseEntity.ok(user);

    }

    private void auth(User user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        Authentication authenticatedUser = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }
}
