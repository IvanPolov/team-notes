package com.gbdevteam.teamnotes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private UUID id;
    private String name;
    private String description;
    private List<NoteDTO> notes;
    private UUID ownerId;
    private Set<UserDTO> users;
    private Set<BoardDTO> boardRoles;
    private Set<ColorDTO> colors;

    public BoardDTO(String name, String description, UUID ownerId) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }

    public BoardDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
