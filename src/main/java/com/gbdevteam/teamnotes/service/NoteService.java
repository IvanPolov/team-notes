package com.gbdevteam.teamnotes.service;


import com.gbdevteam.teamnotes.dto.NoteDTO;
import com.gbdevteam.teamnotes.dto.UserDTO;
import com.gbdevteam.teamnotes.model.Note;

import com.gbdevteam.teamnotes.model.User;
import com.gbdevteam.teamnotes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService implements GenericService<NoteDTO> {

    private final ModelMapper modelMapper;

    private final NoteRepository noteRepository;

    public List<NoteDTO> findAll(UUID boardId){
        return noteRepository.findAllByBoardId(boardId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public NoteDTO findById(UUID id) {
        return convertToDTO(noteRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(
                        "Note with id:" + id + "doesn't exist")));
    }

    @Override
    public UUID create(NoteDTO note) {
        return noteRepository.save(convertToEntity(note)).getId();
    }

    @Override
    public void update(NoteDTO note) {
        noteRepository.save(convertToEntity(note));
    }

    public void deleteById(UUID id) {
        noteRepository.deleteById(id);
    }

    private NoteDTO convertToDTO(Note note){
        return modelMapper.map(note, NoteDTO.class);
    }
    private Note convertToEntity(NoteDTO noteDTO){
        return modelMapper.map(noteDTO, Note.class);
    }
}
