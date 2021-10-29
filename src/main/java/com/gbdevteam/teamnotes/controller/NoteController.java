package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.model.Note;
import com.gbdevteam.teamnotes.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/note")
@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public List<Note> findAll(){
        return noteService.findAll();
    }

    @PostMapping
    public void create(@RequestBody Note note){
        noteService.create(note);
    }

    //example, not implemented
    @PutMapping
    public void update(@RequestBody Note note){
        noteService.update(note);
    }

    //example, not implemented
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Note note){
        noteService.delete(note);
    }

}
