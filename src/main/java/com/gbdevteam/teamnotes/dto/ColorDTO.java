package com.gbdevteam.teamnotes.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public class ColorDTO {

    @NotNull
    @Size(min = 7, max = 7, message = "color length must be 7")
    String colorHex;
    @NotNull
    @Size(max = 30, message = "color description must be less than 30")
    String description;

    UUID boardId;
}
