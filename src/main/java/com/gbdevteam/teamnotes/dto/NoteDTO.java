package com.gbdevteam.teamnotes.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private UUID id;
    private String header;
    private String content;
    private UUID boardId;
    private User creator;
    private Date createDate;
    private Date lastModifiedDate;
}
