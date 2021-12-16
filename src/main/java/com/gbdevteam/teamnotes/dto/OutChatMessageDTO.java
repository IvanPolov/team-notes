package com.gbdevteam.teamnotes.dto;

import com.gbdevteam.teamnotes.util.ChatCommands;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OutChatMessageDTO {

    private Long id;

    private String message;

    private String senderName;

    private Date sentMessageDate;

    private String boardID;

    private ChatCommands command;

}
