package com.gbdevteam.teamnotes.repository;

import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findAllByBoardId(UUID boardId, Pageable pageable);

}
