package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.Note;
import com.gbdevteam.teamnotes.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public List<Board> findAll(){
        log.info("findAllBoards()");
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Board> getOneBoardById(@PathVariable UUID id) throws NoSuchElementException {
        boardService.findById(id).orElseThrow(() ->
                new NoSuchElementException(
                        "Board doesn't exist with id: " + id));
        return boardService.findById(id);
    }

    @GetMapping("/{id}/notes")
    public List<Note> getNotesByBoardId(@PathVariable UUID id){
        return boardService.findNotes(id);
    }

    @PostMapping
    public UUID create(@RequestBody Board board) {
        return boardService.create(board);
    }

    @PutMapping
    public void update(@RequestBody Board board){
       boardService.update(board);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        boardService.deleteById(id);
    }
}
