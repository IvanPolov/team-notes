package com.gbdevteam.teamnotes.service;


import com.gbdevteam.teamnotes.dto.NoteDTO;
import com.gbdevteam.teamnotes.model.Note;
import com.gbdevteam.teamnotes.repository.ChecklistItemRepository;
import com.gbdevteam.teamnotes.repository.ChecklistRepository;
import com.gbdevteam.teamnotes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService implements GenericService<NoteDTO> {

    private final ModelMapper modelMapper;

    private final NoteRepository noteRepository;
    private final ChecklistItemRepository checklistItemRepository;
    private final ChecklistRepository checklistRepository;
    private final ColorService colorService;
    private final UserService userService;

    public List<NoteDTO> findAll(UUID boardId) {
        return noteRepository.findAllByBoardId(boardId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public NoteDTO findById(UUID id) {
        return convertToDTO(noteRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(
                        "Note with id:" + id + "doesn't exist")));
    }

    @Override
    public UUID create(NoteDTO noteDTO) {
        Note note = convertToEntity(noteDTO);
        if(note.getChecklists() != null) {
            checklistRepository.saveAll(note.getChecklists());
        }
        Note savedNote = noteRepository.save(note);
        log.info(savedNote.getCreateDate().toString());
        return savedNote.getId();
    }

    @Override
    public void update(NoteDTO note) {
        noteRepository.save(convertToEntity(note));
    }

    public void deleteById(UUID id) {
        noteRepository.deleteById(id);
    }

    public void deleteAll(UUID boardId) {
        noteRepository.deleteByBoardId(boardId);
    }

    private NoteDTO convertToDTO(Note note) {
        return modelMapper.map(note, NoteDTO.class);
    }

    private Note convertToEntity(NoteDTO noteDTO) {
        return modelMapper.map(noteDTO, Note.class);
    }
}
