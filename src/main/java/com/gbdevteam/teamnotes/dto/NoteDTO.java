package com.gbdevteam.teamnotes.dto;

import com.gbdevteam.teamnotes.model.User;
import lombok.*;
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
    private String color;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date lastModifiedDate;
}
