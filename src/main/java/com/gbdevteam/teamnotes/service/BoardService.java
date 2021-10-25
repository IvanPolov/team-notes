package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.repository.BoardRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService implements GenericService<Board> {

    private final BoardRepository boardRepository;

    @Override
    public List<Board> findAll() {
       return boardRepository.findAll();
    }

    @Override
    public void findById(UUID id) {
        boardRepository.findById(id);
    }

    @Override
    public void delete(Board board) {
        boardRepository.delete(board);
    }

    @Override
    public void create(Board board) {
        boardRepository.save(board);
    }

    @Override
    public void update(Board board) {
        boardRepository.save(board);
    }
}
