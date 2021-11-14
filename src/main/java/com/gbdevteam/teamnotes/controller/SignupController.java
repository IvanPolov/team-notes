package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.UserRegAuthDto;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.World;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private ModelMapper mapper;

    @PostMapping
    @PreAuthorize("permitAll()")
    public UUID save(@Validated @RequestBody UserRegAuthDto userRegAuthDto) throws Exception  {
        log.info(userRegAuthDto.getEmail());
        if (userService.findByEmail(userRegAuthDto.getEmail()) != null) throw new Exception ("Email all exitst in DB");
        User user = new User(userRegAuthDto);
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
