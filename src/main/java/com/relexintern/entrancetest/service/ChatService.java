package com.relexintern.entrancetest.service;

import com.relexintern.entrancetest.models.Chat;
import com.relexintern.entrancetest.models.User;
import com.relexintern.entrancetest.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    @Autowired
    ChatRepository chatRepository;

    public Chat getChatByParticipants(User user1, User user2) {
        if (user1 == null || user2 == null)
            return null;

        if (user1.getUsername().equals(user2.getUsername()))
            return null;

        var chat = chatRepository.getChatByUser1AndUser2(user1, user2);

        if (chat == null)
            chat = chatRepository.getChatByUser1AndUser2(user2, user1);

        if (chat == null) {
            chat = new Chat(user1, user2);
            chatRepository.save(chat);
        }

        return chat;
    }
}
