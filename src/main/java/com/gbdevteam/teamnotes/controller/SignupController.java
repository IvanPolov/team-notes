package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public UUID save(@RequestBody User user){
        log.info(user.getEmail());
        return userService.create(user);
    }

//    @GetMapping
//    @ResponseBody
//    public String getEmail(@PathVariable String email){
//        if(userService.findByEmail(email) == null)
//        return "email is already exists";
//        return "";
//    }
}
