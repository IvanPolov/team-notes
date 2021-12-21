package com.gbdevteam.teamnotes.repository;

import com.gbdevteam.teamnotes.model.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
}
