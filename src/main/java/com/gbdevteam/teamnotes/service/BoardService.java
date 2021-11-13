package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.Note;
import com.gbdevteam.teamnotes.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService implements GenericService<Board> {

    private final BoardRepository boardRepository;
    private final NoteService noteService;
    private final UserService userService;

    public List<Board> findAll(UUID userId) {
       List<Board> allBoards = boardRepository.findAllByOwnerId(userId);
       allBoards.addAll(boardRepository.findAllByUsers_Id(userId));
       return allBoards;
    }

    public Optional<Board> findById(UUID id) {
        return boardRepository.findById(id);
    }

    public UUID create(Board board) {
       return boardRepository.save(board).getId();
    }

    public void update(Board board) {
        boardRepository.save(board);
    }

    public void deleteById(UUID id) {
        boardRepository.deleteById(id);
    }

    public List<Note> findNotes(UUID boardId){
        return noteService.findAll(boardId);
    }

    public void addUser(UUID boardId, UUID userId){
        Board board =  boardRepository.findById(boardId).orElseThrow(()-> new NoSuchElementException("Board service exception. Board not found."));
        board.setUser(userService.findById(userId).orElseThrow(()-> new NoSuchElementException("user with that id not found")));
        update(board);
    }

    @PostConstruct
    public void init(){
        boardRepository.save(new Board("my board 1","custom description",userService.findByEmail("test@mail.com")));
        boardRepository.save(new Board("my board 2","random text",userService.findByEmail("test@mail.com")));
        boardRepository.save(new Board("my board 3","my amazing board description",userService.findByEmail("test2@mail.com")));
    }
}