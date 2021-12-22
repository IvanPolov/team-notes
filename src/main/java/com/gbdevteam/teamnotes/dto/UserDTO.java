package com.gbdevteam.teamnotes.dto;


import com.gbdevteam.teamnotes.model.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank(message = "Name must not be blank")
    private UUID id;
    @NotBlank(message = "Name must not be blank")
    private String username;
    @Email
    @NotBlank(message = "Name must not be blank")
    private String email;

    private Boolean isVerified;

    private Set<Board> myBoards;

    private Set<Board> boards;
}
