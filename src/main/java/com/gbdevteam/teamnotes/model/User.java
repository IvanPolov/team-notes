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
import java.util.*;

@Data
@Entity
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String username;

    @Column(unique = true)
    private String email;

    private Boolean isVerified;

    private String password;

    @Transient
    private String confirmPassword;

    @OneToMany(mappedBy = "owner")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnoreProperties("owner")
    private Set<Board> myBoards = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    @JsonIgnoreProperties("users")
    private Set<Board> boards;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public void setMyBoards(Board board){
        myBoards.add(board);
    }

    public User(String email, String username, Boolean isVerified, String password, List<Role> roles) {
        this.email = email;
        this.username = username;
        this.isVerified = isVerified;
        this.password = password;
        this.roles = roles;
    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
