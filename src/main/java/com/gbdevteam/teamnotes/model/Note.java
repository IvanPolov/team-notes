package com.gbdevteam.teamnotes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String header;

    @Column(length = 600)
    private String content;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    private Board board;

    @Column(name = "board_id")
    private UUID boardId;

    @ManyToOne
    private User creator;

    private String color;

    private int priority;

    private Boolean isFavorite;

    @CreationTimestamp
    private Date createDate;
    @UpdateTimestamp
    private Date lastModifiedDate;

    public Note(String header, String content, UUID boardId) {
        this.header = header;
        this.content = content;
        this.boardId = boardId;
    }
}