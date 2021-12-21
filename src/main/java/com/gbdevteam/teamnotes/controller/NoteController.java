package com.gbdevteam.teamnotes.controller;

import com.gbdevteam.teamnotes.dto.NoteDTO;
import com.gbdevteam.teamnotes.model.BoardRoleEnum;
import com.gbdevteam.teamnotes.service.BoardRoleService;
import com.gbdevteam.teamnotes.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/note")
@RestController
@RequiredArgsConstructor
@Slf4j
@Validated

public class NoteController {
    private final NoteService noteService;
    private final BoardRoleService boardRoleService;

    @GetMapping
    public List<NoteDTO> findAll() {
        return noteService.findAll(UUID.randomUUID());
    }

    @GetMapping("/{id}")
    public NoteDTO getOneNotedById(@PathVariable("id") UUID id) {
        return noteService.findById(id);
    }

    @PostMapping
    public void create(@Valid @RequestBody NoteDTO note, Principal principal) {
        if (!boardRoleService.checkRole(note.getBoardId(), principal.getName()).equals(BoardRoleEnum.READER)) {
            log.info(note.toString());
            noteService.create(note);
        }
    }

    @PutMapping
    public void update(@Valid @RequestBody NoteDTO note, Principal principal) {
        if (!boardRoleService.checkRole(note.getBoardId(), principal.getName()).equals(BoardRoleEnum.READER)) {
            noteService.update(note);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") UUID id, Principal principal) {
        if (!boardRoleService.checkRole(noteService.findById(id).getBoardId(), principal.getName()).equals(BoardRoleEnum.READER)) {
            noteService.deleteById(id);
        }
    }

}
