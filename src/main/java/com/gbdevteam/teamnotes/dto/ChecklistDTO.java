package com.gbdevteam.teamnotes.dto;

import com.gbdevteam.teamnotes.model.ChecklistItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
public class ChecklistDTO {

    private Long id;

    private String name;

    private List<ChecklistItemDTO> items;
}
