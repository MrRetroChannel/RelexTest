package com.relexintern.entrancetest.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "messages")
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "from_id")
    @JsonIgnore
    @Getter
    User from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    @JsonIgnore
    @Getter
    User to;

    @Getter
    private String contents;

    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private Date time;

    public Message(Chat chat, String contents, User from, User to) {
        this.from = from;
        this.to = to;
        this.chat = chat;
        this.contents = contents;
        this.time = new Date();
    }

    @JsonSerialize
    public String getSender() {
        return from.getUsername();
    }

    @JsonSerialize
    public String getReceiver() {
        return to.getUsername();
    }
}
