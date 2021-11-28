package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.dto.BoardDTO;
import com.gbdevteam.teamnotes.dto.BoardRoleDTO;
import com.gbdevteam.teamnotes.dto.NoteDTO;
import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.BoardRoleEnum;
import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService implements GenericService<BoardDTO> {

    private final BoardRepository boardRepository;
    private final NoteService noteService;
    private final UserService userService;
    private final RoleService roleService;
    private final BoardRoleService boardRoleService;
    private final ModelMapper modelMapper;

    public List<BoardDTO> findAll(UUID userId) {
        List<BoardDTO> allBoards = boardRepository.findAllByOwnerId(userId).stream().map(this::convertToDTO).collect(Collectors.toList());
        allBoards.addAll(boardRepository.findAllByUsers_Id(userId).stream().map(this::convertToDTO).collect(Collectors.toList()));
        return allBoards;
    }

    public BoardDTO findById(UUID id) {
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(
                        "Board doesn't exist with id: " + id));
        return convertToDTO(board);
    }

    public UUID create(BoardDTO boardDTO) {
        Board dbBoard = boardRepository.save(convertToEntity(boardDTO));
        boardRoleService.create(new BoardRoleDTO(dbBoard.getId(), dbBoard.getOwnerId(), BoardRoleEnum.OWNER));
        return dbBoard.getId();
    }

    public void update(BoardDTO board) {
        update(convertToEntity(board));
    }

    public void update(Board board) {
        boardRepository.save(board);
    }

    public void deleteById(UUID id) {
//        noteService.deleteAll(id);
        boardRepository.deleteById(id);
    }

    public List<NoteDTO> findNotes(UUID boardId) {
        return noteService.findAll(boardId);
    }

    public void addUser(UUID boardId, UUID userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException("Board service exception. Board not found."));
        board.setUser(userService.findUserById(userId));
        boardRoleService.create(new BoardRoleDTO(boardId, userId, BoardRoleEnum.READER));
        update(board);
    }

    public void removeUser(UUID boardId, UUID userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException("Board service exception. Board not found."));
        board.removeUser(userId);
        boardRoleService.deleteByBoardIdAndUserId(boardId, userId);
        update(board);
    }

    private BoardDTO convertToDTO(Board board) {
        return modelMapper.map(board, BoardDTO.class);
    }

    private Board convertToEntity(BoardDTO boardDTO) {
        return modelMapper.map(boardDTO, Board.class);
    }

    @PostConstruct
    public void init() {
        userService.save(new User("test@mail.com", "test", true, userService.passwordEncoder().encode("12345"), Arrays.asList(roleService.findByName("USER"))));
        userService.save(new User("test2@mail.com", "test2", true, userService.passwordEncoder().encode("12345"), Arrays.asList(roleService.findByName("USER"))));
        this.create(new BoardDTO("my board 1", "custom description", userService.findByEmail("test@mail.com").getId()));
        this.create(new BoardDTO("my board 2", "random text", userService.findByEmail("test@mail.com").getId()));
        this.create(new BoardDTO("my board 3", "my amazing board description", userService.findByEmail("test2@mail.com").getId()));

    }
}