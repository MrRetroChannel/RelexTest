package com.relexintern.entrancetest.repository;

import com.relexintern.entrancetest.models.Chat;
import com.relexintern.entrancetest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat getChatByUser1AndUser2(User user1, User user2);
}
