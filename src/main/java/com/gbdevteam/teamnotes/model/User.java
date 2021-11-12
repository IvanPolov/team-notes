package com.gbdevteam.teamnotes.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class User {
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

    @OneToMany
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
}
