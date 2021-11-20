package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.BoardRoleDTO;
import com.gbdevteam.teamnotes.model.BoardRoleEnum;
import com.gbdevteam.teamnotes.service.BoardRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RequestMapping("/api/v1/board-roles")
@RestController
@RequiredArgsConstructor
public class BoardRoleController {

    private final BoardRoleService boardRoleService;

    @GetMapping("/{boardId}")
    public List<BoardRoleDTO> getRoles(@PathVariable UUID boardId){
        return boardRoleService.findAll(boardId);
    }

    @GetMapping("/types")
    public BoardRoleEnum[] getRoleTypes(){
        return BoardRoleEnum.values();
    }

    @GetMapping("/{boardId}/users/{userId}/")
    public BoardRoleDTO getBoardUserRole(@PathVariable UUID boardId, @PathVariable UUID userId){
        return boardRoleService.findBoardUserRole(boardId,userId);
    }

    @PostMapping("/set")
    public void setBoardUserRole(@RequestBody BoardRoleDTO boardRoleDTO){
        boardRoleService.create(boardRoleDTO);
    }
}
