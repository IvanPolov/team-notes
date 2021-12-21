package com.gbdevteam.teamnotes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChecklistItemDTO {

    private Long id;

    private String content;

    private Boolean isCompleted;
}
