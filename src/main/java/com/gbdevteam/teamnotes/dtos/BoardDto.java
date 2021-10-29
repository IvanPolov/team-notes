package com.gbdevteam.teamnotes.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;
import com.gbdevteam.teamnotes.model.Board;

import java.util.UUID;

@Data
@NoArgsConstructor
public class BoardDto {

    private UUID id;
    private String name;
    private String description;

    public BoardDto(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.description = board.getDescription();
    }
}
