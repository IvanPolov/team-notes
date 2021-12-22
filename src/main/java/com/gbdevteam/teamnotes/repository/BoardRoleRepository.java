package com.gbdevteam.teamnotes.repository;

import com.gbdevteam.teamnotes.model.BoardRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoardRoleRepository extends JpaRepository<BoardRole, UUID> {
    List<BoardRole> findAllByBoardId(UUID boardId);

    BoardRole findByBoardIdAndUserId(UUID boardId, UUID userId);

    void deleteByBoardIdAndUserId(UUID boardId, UUID userId);
}
