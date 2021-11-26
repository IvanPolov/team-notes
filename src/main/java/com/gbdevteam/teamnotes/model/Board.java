package com.gbdevteam.teamnotes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
public class Board {
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
    @JsonIgnoreProperties("myBoards")
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private User owner;

    @Column(name = "owner_id")
    private UUID ownerId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("boards")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinTable(name = "boards_users",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    public void setUser(User user) {
        users.add(user);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "board")
    @JsonIgnoreProperties("board")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    public Set<BoardRole> boardRoles;

    public void removeUser(UUID userId) {
        Optional<User> user = users.stream().filter(u -> u.getId().equals(userId)).findFirst();
        user.ifPresent(value -> users.remove(value));
    }

    public Board(String name, String description, UUID ownerId) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }

}
