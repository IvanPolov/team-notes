package com.gbdevteam.teamnotes.repository;

import com.gbdevteam.teamnotes.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ColorRepository extends JpaRepository<Color, UUID> {
    Optional<Color> findByColorHexAndBoard_Id(String colorHex, UUID boardId);
}
