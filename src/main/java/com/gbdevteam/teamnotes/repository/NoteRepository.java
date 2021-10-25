package com.gbdevteam.teamnotes.repository;

import com.gbdevteam.teamnotes.model.Note;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
}
