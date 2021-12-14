package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.dto.BoardDTO;
import com.gbdevteam.teamnotes.dto.ChatMessageDTO;
import com.gbdevteam.teamnotes.dto.OutChatMessageDTO;
import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.chat.ChatMessage;
import com.gbdevteam.teamnotes.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ModelMapper modelMapper;
    private final BoardService boardService;

    @Transactional
    public OutChatMessageDTO save(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = new ChatMessage();
        modelMapper.map(chatMessageDTO, chatMessage);
        chatMessage.setSentMessageDate(new Date());
        BoardDTO boardDTO = boardService.findById(UUID.fromString(chatMessageDTO.getCurrenBoardId()));
        log.info(">>>>>>>>>>>>>>>>>>>> CurrenBoardId " + boardDTO.getId().toString());
        Board board = new Board();
        modelMapper.map(boardDTO, board);
        chatMessage.setBoard(board);
        log.info(">>>>>>>>>>>>>>>>>>>> chatMessage " + chatMessage);
        chatMessageRepository.save(chatMessage);
        log.info(">>>>>>>>>>>>>>>>>>>> chatMessage " + chatMessage);
        OutChatMessageDTO outMessage = new OutChatMessageDTO();
        modelMapper.map(chatMessage, outMessage);
        outMessage.setBoardID(chatMessage.getBoard().getId().toString());
        return outMessage;
    }

    public Page<ChatMessage> findNewChatMessagePage(UUID boardId, int page, int pageSize) {
        log.info(boardId.toString());
        return chatMessageRepository.findAllByBoardId(boardId, PageRequest.of(page, pageSize));
    }
}
