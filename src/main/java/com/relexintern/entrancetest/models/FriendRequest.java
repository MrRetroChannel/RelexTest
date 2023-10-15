package com.relexintern.entrancetest.models;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_request_id")
    public Long friendRequestId;

    @OneToOne
    private User from;

    @OneToOne
    private User to;

    public FriendRequest(User from, User to) {
        this.from = from;
        this.to = to;
    }
}
