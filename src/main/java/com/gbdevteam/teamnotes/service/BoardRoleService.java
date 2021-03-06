package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.dto.BoardRoleDTO;
import com.gbdevteam.teamnotes.model.BoardRole;
import com.gbdevteam.teamnotes.model.BoardRoleEnum;
import com.gbdevteam.teamnotes.repository.BoardRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardRoleService {

    private final ModelMapper modelMapper;

    private final BoardRoleRepository boardRoleRepository;
    private final UserService userService;

    public List<BoardRoleDTO> findAll(UUID boardId) {
        return boardRoleRepository.findAllByBoardId(boardId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BoardRoleDTO findBoardUserRole(UUID boardId, UUID userId) {
        return convertToDTO(boardRoleRepository.findByBoardIdAndUserId(boardId, userId));
    }

    public BoardRoleEnum checkRole(UUID boardId, String email) {
        UUID userID = userService.findByEmail(email).getId();
        return findBoardUserRole(boardId, userID).getRole();
    }

    public void save(BoardRoleDTO boardRoleDTO) {
//        BoardRole boardRole = boardRoleRepository.findByBoardIdAndUserId(boardRoleDTO.getBoardId(), boardRoleDTO.getUserId());
//        if(boardRole != null) boardRole.setRole(boardRoleDTO.getRole());
        boardRoleRepository.save(convertToEntity(boardRoleDTO));
    }

    public void deleteByBoardIdAndUserId(UUID boarId, UUID userId) {
        boardRoleRepository.deleteByBoardIdAndUserId(boarId, userId);
    }

    private BoardRoleDTO convertToDTO(BoardRole boardRole) {
        return modelMapper.map(boardRole, BoardRoleDTO.class);
    }

    private BoardRole convertToEntity(BoardRoleDTO boardRoleDTO) {
        return modelMapper.map(boardRoleDTO, BoardRole.class);
    }
}
