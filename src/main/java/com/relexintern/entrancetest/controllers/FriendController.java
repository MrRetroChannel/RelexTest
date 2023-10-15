package com.relexintern.entrancetest.controllers;

import com.relexintern.entrancetest.models.FriendRequest;
import com.relexintern.entrancetest.models.User;
import com.relexintern.entrancetest.repository.FriendRequestRepository;
import com.relexintern.entrancetest.requests.ToFriendsRequest;
import com.relexintern.entrancetest.service.UserService;
import com.relexintern.entrancetest.util.SessionUtil;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    @Autowired
    FriendRequestRepository requestRepository;

    @Autowired
    UserService userService;

    @PostMapping("/sendRequest")
    public ResponseEntity<?> sendFriendRequest(@RequestBody ToFriendsRequest request) {
        var curUser = userService.getUserByUsername(SessionUtil.getUsernameFromSession());
        var destUser = userService.getUserByUsername(request.getUsername());

        if (destUser == null)
            return ResponseEntity.badRequest().body("Пользователь не существует");

        if (requestRepository.findByFromAndTo(curUser, destUser) != null)
            return ResponseEntity.badRequest().body("Заявка в друзья уже отправлена");

        if (destUser.getFriends().contains(curUser))
            return ResponseEntity.badRequest().body("Вы уже друзья");

        var friendRequest = new FriendRequest(curUser, destUser);
        requestRepository.save(friendRequest);

        return ResponseEntity.ok("Заявка в друзья " + destUser.getUsername() + " отправлена");
    }

    @GetMapping("/acceptRequest")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam String username) {
        var curUser = userService.getUserByUsername(SessionUtil.getUsernameFromSession());
        var sender = userService.getUserByUsername(username);

        var request = requestRepository.findByFromAndTo(sender, curUser);

        if (request == null)
            return ResponseEntity.badRequest().body("Данный пользователь не отправлял вам заявку в друзья");

        sender.addFriend(curUser);
        curUser.addFriend(sender);

        userService.editUser(sender);
        userService.editUser(curUser);

        requestRepository.delete(request);

        return ResponseEntity.ok("Заявка в друзья от " + username + " принята");
    }

    @GetMapping("/getFriends")
    public ResponseEntity<?> getAllFriends(@RequestParam(required = false) String username) {
        var user = userService.getUserByUsername(username);
        var curUser = userService.getUserByUsername(SessionUtil.getUsernameFromSession());
        if (user == null) {
            user = curUser;
            return ResponseEntity.ok(user.getFriends().stream().map(User::getUsername).collect(Collectors.toList()));
        }

        if (user.isFriend(curUser))
            return ResponseEntity.ok(user.getFriends().stream().map(User::getUsername).collect(Collectors.toList()));

        return ResponseEntity.badRequest().body("Друзья пользователя скрыты");
    }

    @GetMapping("/setPrivate")
    public ResponseEntity<?> setPrivate(@RequestParam boolean set) {
        var curUser = userService.getUserByUsername(SessionUtil.getUsernameFromSession());
        curUser.setOnlyFriendsMessaging(set);

        userService.editUser(curUser);

        return ResponseEntity.ok(set);
    }
}
