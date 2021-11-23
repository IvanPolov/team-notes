package com.gbdevteam.teamnotes.service;


import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public interface GenericService<T> {

    T findById(UUID id);
    UUID create(T t);
    void update(T t);
    void deleteById(UUID id);
}
