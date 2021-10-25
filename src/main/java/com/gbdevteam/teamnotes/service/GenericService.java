package com.gbdevteam.teamnotes.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface GenericService<T> {

    List<T> findAll();

    void findById(UUID id);

    void delete(T t);

    void create(T t);

    void update(T t);
}
