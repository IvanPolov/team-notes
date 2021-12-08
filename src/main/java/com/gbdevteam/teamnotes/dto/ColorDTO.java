package com.gbdevteam.teamnotes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorDTO {

    @NotNull
    @Size(min = 7, max = 7, message = "color length must be 7")
    private String colorHex;
    @NotNull
    @Size(max = 30, message = "color description must be less than 30")
    private String description;

    private UUID boardId;
}
