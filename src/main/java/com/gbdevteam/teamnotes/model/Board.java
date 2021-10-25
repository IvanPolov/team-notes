package com.gbdevteam.teamnotes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Board {
    @Id
    private UUID id;

    private String name;

    private String description;


    //next split
    @ManyToOne
    private User owner;

}
