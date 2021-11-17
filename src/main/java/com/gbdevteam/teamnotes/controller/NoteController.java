package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.model.Note;
import com.gbdevteam.teamnotes.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/note")
@RestController
@RequiredArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public List<Note> findAll(){
        return noteService.findAll(UUID.randomUUID());
    }

    @GetMapping("/{id}")
    public Optional<Note> getOneNotedById(@PathVariable UUID id) throws Throwable {
        noteService.findById(id).orElseThrow(() ->
                new NoSuchElementException(
                        "Note with id:" + id + "doesn't exist"));
        return noteService.findById(id);
    }

    @PostMapping
    public void create(@RequestBody Note note){
        noteService.create(note);
    }

    @PutMapping
    public void update(@RequestBody Note note){
        noteService.update(note);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id){
        noteService.deleteById(id);
    }

}
