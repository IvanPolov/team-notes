package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.dto.BoardDTO;
import com.gbdevteam.teamnotes.dto.ChatMessageDTO;
import com.gbdevteam.teamnotes.dto.OutChatMessageDTO;
import com.gbdevteam.teamnotes.dto.OutHistoryChatMessageDTO;
import com.gbdevteam.teamnotes.model.Board;
import com.gbdevteam.teamnotes.model.chat.ChatMessage;
import com.gbdevteam.teamnotes.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ModelMapper modelMapper;
    private final BoardService boardService;
    private final int PAGE_SIZE = 10;

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

    public OutHistoryChatMessageDTO findNewChatMessage(UUID boardId) {
        ChatMessage chatMessage = chatMessageRepository.findFirstByBoardIdOrderByIdDesc(boardId);
        return modelMapper.map(chatMessage, OutHistoryChatMessageDTO.class);
    }

    public Page<OutHistoryChatMessageDTO> findNewChatMessagePage(UUID boardId) {
//        log.info(boardId.toString());
//        int previousMessageId = Math.max(chatId - PAGE_SIZE, 0);
//        if (chatId < 0) {  //   if chatId=-1 than get last one messsage and put into page.
//            chatId = chatMessageRepository.findTopByBoardId(boardId);
//            previousMessageId = chatId;
//        } else if (chatId == 0) { //   if chatId=0 than get last 10 messsages and put into page.
//            chatId = chatMessageRepository.fin().lastIndexOf();.findTopByBoardId(boardId);

//        List<ChatMessage> chatMessagePage = chatMessageRepository.findChatMessageByBoardIdAndIdIsLessThanAndIdGreaterThan(boardId, chatId, previousMessageId);


        Page<ChatMessage> chatMessagePage = chatMessageRepository.findAllByBoardId(boardId, PageRequest.of(0, PAGE_SIZE));
        Page<OutHistoryChatMessageDTO> outHistoryMessages = new PageImpl<>(chatMessagePage.getContent().stream().map(s -> modelMapper.map(s, OutHistoryChatMessageDTO.class)).collect(Collectors.toList()), chatMessagePage.getPageable(), chatMessagePage.getTotalElements());
        return outHistoryMessages;
    }


}
