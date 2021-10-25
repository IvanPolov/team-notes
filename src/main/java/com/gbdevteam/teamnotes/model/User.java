package com.gbdevteam.teamnotes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    private UUID id;

    private String username;

    private String email;

    private Boolean isVerified;

    private String password;

    @OneToMany
    private List<Board> boards;
}
