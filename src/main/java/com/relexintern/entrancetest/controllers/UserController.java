package com.relexintern.entrancetest.controllers;

import com.relexintern.entrancetest.models.RoleType;
import com.relexintern.entrancetest.models.User;
import com.relexintern.entrancetest.requests.ChangePasswordRequest;
import com.relexintern.entrancetest.service.EmailService;
import com.relexintern.entrancetest.service.UserService;
import com.relexintern.entrancetest.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailService emailService;

    @GetMapping("/getUsers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editUserData(@RequestBody Map<String, String> params){
        String email = params.get("email");
        String name = params.get("name");
        String surname = params.get("surname");
        String username = params.get("username");

        User curUser = userService.getUserByUsername(SessionUtil.getUsernameFromSession());

        if (email != null && !userService.emailExists(email)) {
            curUser.setEmail(email);
            curUser.removeRole(RoleType.CONFIRMED);
            emailService.sendConfirmationMail(curUser);
        }

        if (name != null)
            curUser.setName(name);

        if (surname != null)
            curUser.setSurname(surname);

        if (username != null && !userService.usernameExists(username))
            curUser.setUsername(username);

        userService.editUser(curUser);

        return ResponseEntity.ok("Все ок");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        User curUser = userService.getUserByUsername(SessionUtil.getUsernameFromSession());

        if (bCryptPasswordEncoder.matches(request.getOldPassword(), curUser.getPassword())) {
            curUser.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
            userService.editUser(curUser);
            return ResponseEntity.ok("Пароль успешно изменен");
        }

        return ResponseEntity.badRequest().body("Неверный текущий пароль");
    }

    @GetMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var curUser = userService.getUserByUsername(SessionUtil.getUsernameFromSession());
        userService.deleteUser(curUser);

        SecurityContextHolder.clearContext();

        if (request.getSession(false) != null)
            request.getSession().invalidate();

        response.sendRedirect(request.getContextPath() + "/");

        return ResponseEntity.ok("Аккаунт успешно удален");
    }
}