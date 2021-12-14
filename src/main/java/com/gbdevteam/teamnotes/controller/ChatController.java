package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.ChatMessageDTO;
import com.gbdevteam.teamnotes.dto.OutChatMessageDTO;
import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.chat.ChatMessage;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.model.chat.ChatNotification;
import com.gbdevteam.teamnotes.service.BoardService;
import com.gbdevteam.teamnotes.service.ChatMessageService;
import com.gbdevteam.teamnotes.service.ChatRoomService;
import com.gbdevteam.teamnotes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    public void processMessage(@Headers @Payload ChatMessageDTO chatMessage, Principal principal) throws IllegalAccessException {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Recived new request for handle sended new message!");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
            messagingTemplate.convertAndSendToUser(
                    saved.getBoardID(), "/secured/topic",
                    saved);
        } else {
            throw new IllegalAccessException();
        }
    }


    @GetMapping("/messages")
    public ResponseEntity<?> findChatMessages(@RequestParam("chatId") String boardId) {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Recived new request for show all messages!");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Recived request from BoardID: " + boardId);
        int page, pageSize;
        page = 1;
        pageSize = 1;
        return ResponseEntity
                .ok(chatMessageService.findNewChatMessagePage(UUID.fromString(boardId), page, pageSize));
    }
}