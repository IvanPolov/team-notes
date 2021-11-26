package com.gbdevteam.teamnotes.dto;

import com.gbdevteam.teamnotes.model.BoardRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardRoleDTO {

    private UUID boardId;
    private UUID userId;
    private BoardRoleEnum role;
}
