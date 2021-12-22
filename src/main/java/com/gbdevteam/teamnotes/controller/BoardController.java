package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.controller.validators.ValidUUID;
import com.gbdevteam.teamnotes.dto.BoardDTO;
import com.gbdevteam.teamnotes.dto.NoteDTO;
import com.gbdevteam.teamnotes.model.BoardRoleEnum;
import com.gbdevteam.teamnotes.service.BoardRoleService;
import com.gbdevteam.teamnotes.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BoardController {
    private final BoardService boardService;
    private final BoardRoleService boardRoleService;

    @GetMapping("/user/{userId}")
    public List<BoardDTO> findAll(@PathVariable("userId") @ValidUUID UUID userId) {
        log.info("findAllBoards()");
        return boardService.findAll(userId);
    }

    @GetMapping("/{id}")
    public BoardDTO getOneBoardById(@PathVariable("id") @ValidUUID UUID id) throws NoSuchElementException {
        return boardService.findById(id);
    }

    @GetMapping("/{id}/notes")
    public List<NoteDTO> getNotesByBoardId(@PathVariable("id") @ValidUUID  UUID id) {
        return boardService.findNotes(id);
    }

    @PostMapping
    public UUID create(@Valid @RequestBody BoardDTO boardDTO) {
        return boardService.create(boardDTO);
    }

    @PutMapping
    public void update(@Valid @RequestBody BoardDTO board, Principal principal) {
//        BoardRoleEnum boardRole = boardRoleService.checkRole(board.getId(), principal.getName());
//        if(boardRole.equals(BoardRoleEnum.OWNER)) {
            boardService.update(board);
//        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") UUID id, Principal principal) {
        BoardRoleEnum boardRole = boardRoleService.checkRole(id, principal.getName());
        if(boardRole.equals(BoardRoleEnum.OWNER)) {
            boardService.deleteById(id);
        }
    }

    @GetMapping("/{boardId}/addUser/{userId}")
    public void addUser(@PathVariable("boardId") UUID boardId, @PathVariable("userId") UUID userId) {
        boardService.addUser(boardId, userId);
    }

    @DeleteMapping("/{boardId}/removeUser/{userId}")
    public void removeUser(
            @PathVariable("boardId") UUID boardId, @PathVariable("userId") UUID userId) {
        boardService.removeUser(boardId, userId);
    }
}
