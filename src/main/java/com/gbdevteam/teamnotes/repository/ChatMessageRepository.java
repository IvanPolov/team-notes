package com.gbdevteam.teamnotes.repository;


import com.gbdevteam.teamnotes.model.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    ChatMessage findFirstByBoardIdOrderByIdDesc(UUID boardId);

    List<ChatMessage> findChatMessagesByBoardIdAndIdBetween(UUID board_id, Long id, Long id2);

}
