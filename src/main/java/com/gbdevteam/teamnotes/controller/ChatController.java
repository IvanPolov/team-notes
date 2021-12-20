package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.ChatMessageDTO;
import com.gbdevteam.teamnotes.dto.OutChatMessageDTO;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.service.ChatMessageService;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.security.Principal;
import java.util.UUID;

import static java.lang.Long.parseLong;

@Controller
@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {


    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @MessageMapping("/secured/chat")
    public void processMessage(@Headers @Payload ChatMessageDTO chatMessage, Principal principal) throws IllegalAccessException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Received new request for handle sent new message!");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = principal.getName();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Sender name: " + name);
        User currentUser = userService.findByEmail(name);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Current user: " + name);
        chatMessage.setSenderName(name);
        if (currentUser.isChatWriter()) {
            OutChatMessageDTO saved = chatMessageService.save(chatMessage);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> chatMessage: " + saved.toString());
            messagingTemplate.convertAndSend(
                    "/secured/topic/" + saved.getBoardID(),
                    saved);
        } else {
            throw new IllegalAccessException();
        }
    }


    @GetMapping("/messages")
    public ResponseEntity<?> findChatMessages(@RequestParam("chatId") String boardId) {
        log.info(">>>>>>>>>> Recived new request for show all messages!");
        log.info(">>>>>>>>>> Recived request from BoardID: " + boardId);
        return ResponseEntity
                .ok(chatMessageService.findNewChatMessagePage(UUID.fromString(boardId)));
    }

    @GetMapping("/history")
    public ResponseEntity<?> findPreviousChatMessages(@RequestParam("chatId") String boardId, @RequestParam("firstID") String id) {
        log.info(">>>>>>>>>> Recived new request for show all messages!");
        log.info(">>>>>>>>>> Recived request from BoardID: " + boardId);
        Long firstIdL = Long.parseLong(id);
        return ResponseEntity
                .ok(chatMessageService.findHistoryChatMessagePage(UUID.fromString(boardId), firstIdL));
    }


    @GetMapping("/messages/get-one/")
    public ResponseEntity<?> findChatMessage(@RequestParam("chatId") String boardId) {
        log.info(">>>>>>>>>> Received request from BoardID: " + boardId + " to get last message from chat history DB");
        return ResponseEntity
                .ok(chatMessageService.findNewChatMessage(UUID.fromString(boardId)));
    }

}

