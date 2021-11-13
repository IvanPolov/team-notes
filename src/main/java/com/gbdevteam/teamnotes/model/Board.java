package com.gbdevteam.teamnotes.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Board implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Note> notes;

    @ManyToOne
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinTable(name = "boards_users",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    public void setUser(User user) {
        users.add(user);
    }

    public Board(String name, String description, User owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
    }

}
