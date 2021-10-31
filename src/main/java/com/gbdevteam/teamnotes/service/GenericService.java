package com.gbdevteam.teamnotes.service;


import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Service
public interface GenericService<T> {

    Optional<T> findById(UUID id);
    void create(T t);
    void update(T t);
    void deleteById(UUID id);
}
