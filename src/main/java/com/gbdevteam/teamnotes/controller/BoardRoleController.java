package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.BoardRoleDTO;
import com.gbdevteam.teamnotes.model.BoardRoleEnum;
import com.gbdevteam.teamnotes.service.BoardRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/board-roles")
@RestController
@RequiredArgsConstructor
@Validated

public class BoardRoleController {

    private final BoardRoleService boardRoleService;

    @GetMapping("/{boardId}")
    public List<BoardRoleDTO> getRoles(@PathVariable("boardId") UUID boardId) {

        return boardRoleService.findAll(boardId);
    }

    @GetMapping("/types")
    public BoardRoleEnum[] getRoleTypes() {
        return BoardRoleEnum.values();
    }

    @GetMapping("/{boardId}/users/{userId}/")
    public BoardRoleDTO getBoardUserRole(@PathVariable("boardId") UUID boardId, @PathVariable("userId") UUID userId) {
        return boardRoleService.findBoardUserRole(boardId, userId);
    }

    @PostMapping("/set")
    public void setBoardUserRole(@Valid @RequestBody BoardRoleDTO boardRoleDTO, Principal principal) {
        BoardRoleEnum boardRole = boardRoleService.checkRole(boardRoleDTO.getBoardId(), principal.getName());
        if ((boardRole.equals(BoardRoleEnum.OWNER) || boardRole.equals(BoardRoleEnum.MANAGER)) && !boardRoleDTO.getRole().equals(BoardRoleEnum.OWNER) ) {
            boardRoleService.save(boardRoleDTO);
        }
    }
}
