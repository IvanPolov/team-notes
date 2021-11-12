package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.security.Principal;
import java.util.NoSuchElementException;

@RestController
@SessionScope
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
public class UserContorller {

    final UserService userService;

    @GetMapping
    public User getUser(Principal principal){
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        log.info("user: " + user.getUsername());
        return user;
    }

    @GetMapping({"/{email}"})
    public User findByEmail(@PathVariable String email) {
       return userService.findByEmail(email);
    }
}
