package com.gbdevteam.teamnotes.dto;


import com.gbdevteam.teamnotes.model.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
