package com.gbdevteam.teamnotes.service;


import com.gbdevteam.teamnotes.model.Note;

import com.gbdevteam.teamnotes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteService implements GenericService<Note> {

    private final NoteRepository noteRepository;

    public List<Note> findAll(){
        return noteRepository.findAll();
    }

    public Optional<Note> findById(UUID id) {
        return noteRepository.findById(id);
    }

    @Override
    public void create(Note note) {
        noteRepository.save(note);
    }

    @Override
    public void update(Note note) {
        noteRepository.save(note);
    }

    public void deleteById(UUID id) {
        noteRepository.deleteById(id);
    }

    @PostConstruct
    public void init(){
        noteRepository.save(new Note("title","some notes"));
        noteRepository.save(new Note("title2","some notes some notes"));
        noteRepository.save(new Note("title3","some notes some notes some notes some notes"));
    }
}
