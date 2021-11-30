package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.UserDTO;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.BoardService;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@SessionScope
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1/user")
public class UserController {

    final UserService userService;
    final BoardService boardService;

    @GetMapping
    public User getUser(Principal principal) {
        log.info("user: " + principal.getName());
        return userService.findByEmail(principal.getName());
    }

    @GetMapping({"/{email}"})
    public User findByEmail(@PathVariable("email") String email) {
        if (EmailValidator.getInstance().isValid(email)){
            return userService.findByEmail(email);
        }
        return null;
    }

    @GetMapping("/board/{boardId}")
    public List<UserDTO> findAllByBoardId(@PathVariable("boardId") UUID boardId) {
        return userService.findAllByBoardId(boardId);
    }
}
