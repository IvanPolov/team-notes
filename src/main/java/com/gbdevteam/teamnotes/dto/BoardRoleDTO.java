package com.gbdevteam.teamnotes.dto;

import com.gbdevteam.teamnotes.model.BoardRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardRoleDTO {

    @NotNull
    private UUID boardId;
    @NotNull
    private UUID userId;
    @NotNull
    BoardRoleEnum role;
}
