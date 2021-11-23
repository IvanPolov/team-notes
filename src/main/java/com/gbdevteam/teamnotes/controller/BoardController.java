package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.BoardDTO;
import com.gbdevteam.teamnotes.dto.NoteDTO;
import com.gbdevteam.teamnotes.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/user/{userId}")
    public List<BoardDTO> findAll(@PathVariable UUID userId){
        log.info("findAllBoards()");
        return boardService.findAll(userId);
    }

    @GetMapping("/{id}")
    public BoardDTO getOneBoardById(@PathVariable UUID id) throws NoSuchElementException {
       return boardService.findById(id);
    }

    @GetMapping("/{id}/notes")
    public List<NoteDTO> getNotesByBoardId(@PathVariable UUID id){
        return boardService.findNotes(id);
    }

    @PostMapping
    public UUID create(@RequestBody BoardDTO boardDTO) {
        return boardService.create(boardDTO);
    }

    @PutMapping
    public void update(@RequestBody BoardDTO board){
       boardService.update(board);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        boardService.deleteById(id);
    }

    @GetMapping("/{boardId}/addUser/{userId}")
    public void addUser(@PathVariable UUID boardId, @PathVariable UUID userId){
        boardService.addUser(boardId,userId);
    }

    @DeleteMapping("/{boardId}/removeUser/{userId}")
    public void removeUser(@PathVariable UUID boardId, @PathVariable UUID userId){
        boardService.removeUser(boardId,userId);
    }
}
