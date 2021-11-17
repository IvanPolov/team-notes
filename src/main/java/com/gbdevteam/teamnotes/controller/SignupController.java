package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.UserRegAuthDto;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.stream.Collectors;

@RestController
@SessionScope
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Slf4j
@RequestMapping
public class SignupController {

    protected final AuthenticationManager authenticationManager;

    private final UserService userService;

    @PostMapping("/api/v1/signup")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> save(@Validated @RequestBody UserRegAuthDto userRegAuthDto) throws Exception {
        log.info(userRegAuthDto.getEmail());
        if (userService.findByEmail(userRegAuthDto.getEmail()) != null) {
            throw new Exception("Email all ready exist");//TODO handle and send message about this error on front
        }
        User user = new User(userRegAuthDto);
        userService.create(user);
        user = userService.findByEmail(userRegAuthDto.getEmail());
        auth(user);
        return ResponseEntity.ok(user);

    }

    public void auth(User user) {
        new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
