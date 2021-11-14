package com.gbdevteam.teamnotes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
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
    @JsonIgnoreProperties("myBoards")
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private User owner;

    @Column(name = "owner_id")
    private UUID ownerId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("books")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinTable(name = "boards_users",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    public void setUser(User user) {
        users.add(user);
    }

    public Board(String name, String description, UUID ownerId) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }

}
