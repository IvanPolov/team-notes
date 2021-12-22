package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.dto.ColorDTO;
import com.gbdevteam.teamnotes.exception.TeamNotesEntityNotFoundException;
import com.gbdevteam.teamnotes.model.Color;
import com.gbdevteam.teamnotes.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;
    private final ModelMapper modelMapper;

    public void saveOrUpdate(ColorDTO colorDTO) {
        Color color = colorRepository.findByColorHexAndBoard_Id(colorDTO.getColorHex(), colorDTO.getBoardId()).orElseThrow(() -> new TeamNotesEntityNotFoundException("Color info "));
        if (color == null) color = new Color();
        modelMapper.map(colorDTO, color);
        colorRepository.save(color);
    }
}
