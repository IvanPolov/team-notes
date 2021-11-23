package com.gbdevteam.teamnotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BoardRole {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "board_id", nullable = false)
    @JsonIgnoreProperties("boardRoles")
    private UUID boardId;

    @ManyToOne (optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("boardRoles")
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    private Board board;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    private BoardRoleEnum role;
}
