package com.gbdevteam.teamnotes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gbdevteam.teamnotes.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
    private Boolean isFavorite = false;
    @NotNull
    @Min(0)
    @Max(9)
    private int priority;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date lastModifiedDate;
}
