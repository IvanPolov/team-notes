package com.gbdevteam.teamnotes.dto;

import com.gbdevteam.teamnotes.util.ChatCommands;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatMessageDTO {

    private String message;

    private String senderName;

    private String currenBoardId;

    private ChatCommands command;

}
