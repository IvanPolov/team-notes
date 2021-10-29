package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dtos.BoardDto;
import com.gbdevteam.teamnotes.model.Board;
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

    @GetMapping
    public List<Board> findAll(){
        return boardService.findAll();
    }

    @GetMapping("/{id}")
    public BoardDto getOneBoardById(@PathVariable UUID id) {
        Board board = boardService.findById(id).orElseThrow(() ->
                new NoSuchElementException(
                        "Board doesn't exist with id: " + id));
        return new BoardDto(board);
    }

    @PostMapping("/create/{id}")
    public void create(@RequestBody BoardDto boardDto, @RequestParam(name = "id") UUID id) {
        boardService.create(boardDto);
        log.info("create new board " + id);
    }

    //example, not implemented
    @PutMapping
    public BoardDto update(@RequestBody BoardDto boardDto){
        return boardService.update(boardDto);
    }

    //example, not implemented
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        boardService.deleteById(id);
    }

}
