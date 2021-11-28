package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.controller.validators.ValidatorEmail;
import com.gbdevteam.teamnotes.dto.UserDTO;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.BoardService;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;

import javax.validation.constraints.Pattern;
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
    private final ValidatorEmail validatorEmail;

    @GetMapping
    public User getUser(Principal principal) {
        log.info("user: " + principal.getName());
        return userService.findByEmail(principal.getName());
    }

    @GetMapping({"/{email}"})
    public User findByEmail(
            @PathVariable("email")
            @Pattern(regexp = ValidatorEmail.PATTERN_EMAIL)
                    String email) {
        if (validatorEmail.matchByPattern(email, ValidatorEmail.PATTERN_EMAIL)) {
            return userService.findByEmail(email);
        }
        return null;
    }

    @GetMapping("/board/{boardId}")
    public List<UserDTO> findAllByBoardId(@PathVariable("boardId") UUID boardId) {
        return userService.findAllByBoardId(boardId);
    }
}
