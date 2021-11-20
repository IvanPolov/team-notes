package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.dto.BoardRoleDTO;
import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.BoardRoleEnum;
import com.gbdevteam.teamnotes.model.Note;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService implements GenericService<Board> {

    private final BoardRepository boardRepository;
    private final NoteService noteService;
    private final UserService userService;
    private final RoleService roleService;
    private final BoardRoleService boardRoleService;

    public List<Board> findAll(UUID userId) {
        List<Board> allBoards = boardRepository.findAllByOwnerId(userId);
        allBoards.addAll(boardRepository.findAllByUsers_Id(userId));
        return allBoards;
    }

    public Optional<Board> findById(UUID id) {
        return boardRepository.findById(id);
    }

    public UUID create(Board board) {
        Board dbBoard = boardRepository.save(board);
        boardRoleService.create(new BoardRoleDTO(dbBoard.getId(),dbBoard.getOwnerId(), BoardRoleEnum.OWNER));
        return dbBoard.getId();
    }

    public void update(Board board) {
        boardRepository.save(board);
    }

    public void deleteById(UUID id) {
        boardRepository.deleteById(id);
    }

    public List<Note> findNotes(UUID boardId) {
        return noteService.findAll(boardId);
    }

    public void addUser(UUID boardId, UUID userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException("Board service exception. Board not found."));
        board.setUser(userService.findById(userId).orElseThrow(() -> new NoSuchElementException("user with that id not found")));
        boardRoleService.create(new BoardRoleDTO(boardId,userId, BoardRoleEnum.READER));
        update(board);
    }

    public void removeUser(UUID boardId, UUID userId){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException("Board service exception. Board not found."));
        board.removeUser(userId);
        boardRoleService.deleteByBoardIdAndUserId(boardId,userId);
        update(board);
    }

    @PostConstruct
    public void init() {
       userService.save(new User("test@mail.com", "test", true,userService.passwordEncoder().encode("12345"), Arrays.asList(roleService.findByName("USER"))));
       userService.save(new User("test2@mail.com", "test2", true, userService.passwordEncoder().encode("12345"), Arrays.asList(roleService.findByName("USER"))));
       this.create(new Board("my board 1", "custom description", userService.findByEmail("test@mail.com").getId()));
       this.create(new Board("my board 2", "random text", userService.findByEmail("test@mail.com").getId()));
       this.create(new Board("my board 3", "my amazing board description", userService.findByEmail("test2@mail.com").getId()));

    }
}