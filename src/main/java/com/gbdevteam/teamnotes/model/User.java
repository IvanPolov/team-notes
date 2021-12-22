package com.gbdevteam.teamnotes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.ALL;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
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

    private Boolean isVerified = false;

    private String password;

    @OneToMany(mappedBy = "owner", cascade = ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnoreProperties("owner")
    private Set<Board> myBoards = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    @JsonIgnoreProperties("users")
    @ToString.Exclude
    private Set<Board> boards;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    private UUID confirmUUID;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Date dateRegistration;

    private boolean isChatWriter = true;

    public User(String email, String username, Boolean isVerified, String password, List<Role> roles) {
        this.email = email;
        this.username = username;
        this.isVerified = isVerified;
        this.password = password;
        this.roles = roles;
        this.setDateRegistration(new Date());
    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();



    }
}
