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

@Controller
@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {


    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    @MessageMapping("/secured/chat")
//    @SendTo("/secured/topic/")
    public void processMessage(@Headers @Payload ChatMessageDTO chatMessage, Principal principal) throws IllegalAccessException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Recived new request for handle sended new message!");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName();
        String name = principal.getName();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Sender name: " + name);
        User currentUser = userService.findByEmail(name);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Current user: " + name);
        chatMessage.setSenderName(name);
        if (currentUser.isChatWriter()) {
//        if (true) {
            OutChatMessageDTO saved = chatMessageService.save(chatMessage);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> chatMessage: " + saved.toString());
            messagingTemplate.convertAndSend(
                    "/secured/topic/" + saved.getBoardID(),
                    saved);
//            return saved;
        } else {
            throw new IllegalAccessException();
        }
    }


    @GetMapping("/messages")
    public ResponseEntity<?> findChatMessages(@RequestParam("chatId") String boardId) {
        log.info(">>>>>>>>>> Recived new request for show all messages!");
        log.info(">>>>>>>>>> Recived request from BoardID: " + boardId);
        int pageSize;
//        page = 1;
//        pageSize = 10;
        return ResponseEntity
                .ok(chatMessageService.findNewChatMessagePage(UUID.fromString(boardId)));
    }

    @GetMapping("/messages/get-one/")
    public ResponseEntity<?> findChatMessage(@RequestParam("chatId") String boardId) {
        log.info(">>>>>>>>>> Recived request from BoardID: " + boardId + " to get last message from chat history DB");
        int pageSize;
//        page = 1;
//        pageSize = 10;
        return ResponseEntity
                .ok(chatMessageService.findNewChatMessage(UUID.fromString(boardId)));
    }

}

