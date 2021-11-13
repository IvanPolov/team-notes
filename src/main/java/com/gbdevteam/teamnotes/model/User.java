package com.gbdevteam.teamnotes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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

    @OneToMany
    private List<Board> myBoards;

    @ManyToMany(mappedBy = "users")
    @JsonBackReference
    private List<Board> boards;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public User(String email, String username, Boolean isVerified, String password, List<Role> roles) {
        this.email = email;
        this.username = username;
        this.isVerified = isVerified;
        this.password = password;
        this.roles = roles;
    }

    public User(String email, String username, String password, String confirmPassword) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
