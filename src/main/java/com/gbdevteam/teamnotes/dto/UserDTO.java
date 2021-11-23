package com.gbdevteam.teamnotes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.Role;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID id;

    private String username;

    private String email;

    private Boolean isVerified;

    private Set<Board> myBoards;

    private Set<Board> boards;
}
