package com.gbdevteam.teamnotes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gbdevteam.teamnotes.model.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private User creator;
    private String color;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date lastModifiedDate;
}
