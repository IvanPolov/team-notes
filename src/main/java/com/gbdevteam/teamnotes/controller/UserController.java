package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.controller.validators.ValidUUID;
import com.gbdevteam.teamnotes.dto.UserDTO;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.BoardService;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
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
    public User getUser(@NotNull Principal principal) {
        log.info("user: " + principal.getName());
        return userService.findByEmail(principal.getName());
    }

    @GetMapping("/check")
    public ResponseEntity<Object> check(@NotNull Principal principal, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String u = principal.getName();

        if (Boolean.FALSE.equals(userService.findByEmail(u).getIsVerified())) {
            boolean expiredUnverifiedUser = userService.isExpiredUnverifiedUser(userService.findByEmail(u));
            if (expiredUnverifiedUser) {
                userService.deleteById(userService.findByEmail(u).getId());
                request.getSession().invalidate();
                response.sendRedirect("/login");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(userService.findByEmail(u), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(userService.findByEmail(u), HttpStatus.OK);
    }


    @GetMapping({"/{email}"})
    public User findByEmail(@PathVariable("email") String email) {
        if (EmailValidator.getInstance().isValid(email)) {
            return userService.findByEmail(email);
        }
        return null;
    }

    @GetMapping("/board/{boardId}")
    public List<UserDTO> findAllByBoardId(@NotBlank @ValidUUID @PathVariable("boardId") UUID boardId) {
        return userService.findAllByBoardId(boardId);
    }
}
