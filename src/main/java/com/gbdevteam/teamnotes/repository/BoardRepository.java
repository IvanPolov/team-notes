package com.gbdevteam.teamnotes.repository;

import com.gbdevteam.teamnotes.dtos.BoardDto;
import com.gbdevteam.teamnotes.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BoardRepository extends JpaRepository<Board, UUID> {
}
