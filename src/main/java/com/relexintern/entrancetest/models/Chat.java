package com.relexintern.entrancetest.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @OneToOne
    private User user1;

    @OneToOne
    private User user2;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private List<Message> messages;

    public Chat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;

        this.messages = Collections.emptyList();
    }

    public void addMessage(Message message) {
        messages.add(message);
    }
}
