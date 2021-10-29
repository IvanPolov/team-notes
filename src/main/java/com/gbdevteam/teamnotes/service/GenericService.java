package com.gbdevteam.teamnotes.service;

import com.gbdevteam.teamnotes.dtos.BoardDto;
import com.gbdevteam.teamnotes.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface GenericService<T> {

    List<T> findAll();
    void findById(UUID id);
    void create(T t);
    void update(T t);
    void delete(T t);
}
