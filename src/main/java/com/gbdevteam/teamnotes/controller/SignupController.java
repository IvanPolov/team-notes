package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;

import java.util.UUID;

@RestController
@SessionScope
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/signup")
public class SignupController {

    final UserService userService;

    @PostMapping
    public UUID save(@RequestBody User user){
        return userService.create(user);
    }
}
