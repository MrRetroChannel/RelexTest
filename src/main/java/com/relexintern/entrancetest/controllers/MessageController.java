package com.relexintern.entrancetest.controllers;

import com.relexintern.entrancetest.models.Message;
import com.relexintern.entrancetest.repository.ChatRepository;
import com.relexintern.entrancetest.repository.MessageRepository;
import com.relexintern.entrancetest.requests.MessageRequest;
import com.relexintern.entrancetest.service.ChatService;
import com.relexintern.entrancetest.service.UserService;
import com.relexintern.entrancetest.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    ChatService chatService;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @GetMapping("/getMessages")
    public ResponseEntity<?> getAllMessages(@RequestParam String user) {
        var sender = userService.getUserByUsername(SessionUtil.getUsernameFromSession());
        var receiver = userService.getUserByUsername(user);

        var chat = chatService.getChatByParticipants(sender, receiver);
        if (chat == null)
            return ResponseEntity.badRequest().body("Чат не существует");

        if (chat.getMessages().isEmpty())
            return ResponseEntity.badRequest().body("Вы еще не начинали диалог с пользователем");

        return ResponseEntity.ok(chat.getMessages());
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest message) {
        var sender = userService.getUserByUsername(SessionUtil.getUsernameFromSession());
        var receiver = userService.getUserByUsername(message.getReceiver());
        if (receiver == null)
            return ResponseEntity.badRequest().body("Пользователь " + message.getReceiver() + " не найден");

        if (receiver.isOnlyFriendsMessaging() && !receiver.isFriend(sender))
            return ResponseEntity.badRequest().body("Пользователь принимает сообщения только от друзей");

        if (sender.getUsername().equals(receiver.getUsername()))
            return ResponseEntity.badRequest().body("Нельзя отправлять сообщения самому себе");

        var chat = chatService.getChatByParticipants(sender, receiver);

        var msg = new Message(chat, message.getContent(), sender, receiver);

        if (chat.getMessages().isEmpty())
            chat.setMessages(Collections.singletonList(msg));
        else
            chat.addMessage(msg);

        messageRepository.save(msg);
        chatRepository.save(chat);

        return ResponseEntity.ok("Сообщение доставлено");
    }

    @GetMapping("/setFriendsOnly")
    public ResponseEntity<?> setFriendsOnly(@RequestParam boolean set) {
        var curUser = userService.getUserByUsername(SessionUtil.getUsernameFromSession());
        curUser.setOnlyFriendsMessaging(set);

        userService.editUser(curUser);

        return ResponseEntity.ok(set);
    }
}
