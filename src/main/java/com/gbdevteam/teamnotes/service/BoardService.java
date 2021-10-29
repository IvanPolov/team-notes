package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.dtos.BoardDto;
import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final NoteService noteService; //TODO нужна ли связь с записками?

    public List<Board> findAll() {
       return boardRepository.findAll();
    }

    public Optional<Board> findById(UUID id) {
        return boardRepository.findById(id);
    }

    public BoardDto create(BoardDto boardDto) {
        Board board = new Board();
        board.setId(boardDto.getId());
        board.setName(boardDto.getName());
        board.setDescription(boardDto.getDescription());
        boardRepository.save(board);
        return new BoardDto(board);
    }

    public BoardDto update(BoardDto boardDto) {
        Board board = findById(boardDto.getId()).orElseThrow(() ->
        new NoSuchElementException(
                "Board with id " +
                        boardDto.getId() +
                        "doesn't exist " +
                        "(update operation)"));
        board.setId(boardDto.getId());
        board.setName(boardDto.getName());
        board.setDescription(boardDto.getDescription());
        return new BoardDto(board);
    }

    public void deleteById(UUID id) {
        boardRepository.deleteById(id);
    }
}
