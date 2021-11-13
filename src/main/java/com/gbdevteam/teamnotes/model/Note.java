package com.gbdevteam.teamnotes.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Note implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String header;

    @Column(length = 600)
    private String content;//second split String -> class Content

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

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date createDate;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date lastModifiedDate;

    public Note(String header, String content, UUID boardId){
        this.header = header;
        this.content = content;
        this.boardId = boardId;
    }
}