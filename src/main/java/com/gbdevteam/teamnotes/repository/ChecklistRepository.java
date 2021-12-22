package com.gbdevteam.teamnotes.repository;

import com.gbdevteam.teamnotes.model.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistRepository extends JpaRepository<Checklist,Long> {
}
