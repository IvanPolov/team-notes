package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService implements GenericService<Board> {

    private final BoardRepository boardRepository;
    private final NoteService noteService; //TODO для связи с записками

    public List<Board> findAll() {
       return boardRepository.findAll();
    }

    public Optional<Board> findById(UUID id) {
        return boardRepository.findById(id);
    }

    public void create(Board board) {
        boardRepository.save(board);
    }

    public void update(Board board) {
        boardRepository.save(board);
    }

    public void deleteById(UUID id) {
        boardRepository.deleteById(id);
    }
}