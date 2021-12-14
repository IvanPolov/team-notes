package com.gbdevteam.teamnotes.model.chat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gbdevteam.teamnotes.model.Board;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    private String senderName;

    private Date sentMessageDate;
@ManyToOne
    private Board board;

}
