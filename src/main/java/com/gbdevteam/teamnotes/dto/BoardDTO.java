package com.gbdevteam.teamnotes.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gbdevteam.teamnotes.model.BoardRole;
import com.gbdevteam.teamnotes.model.Note;
import com.gbdevteam.teamnotes.model.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
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
    private List<Note> notes;
    private UUID ownerId;
    private Set<User> users;
    public Set<BoardRole> boardRoles;

    public BoardDTO(String name, String description, UUID ownerId) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }
}
