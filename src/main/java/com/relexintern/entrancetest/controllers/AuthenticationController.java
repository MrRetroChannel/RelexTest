package com.relexintern.entrancetest.controllers;

import com.relexintern.entrancetest.requests.JwtAuthenticationResponse;
import com.relexintern.entrancetest.requests.SignInRequest;
import com.relexintern.entrancetest.requests.SignUpRequest;
import com.relexintern.entrancetest.service.AuthenticationService;
import com.relexintern.entrancetest.service.UserService;
import com.relexintern.entrancetest.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        var token = authService.signUp(request);

        if (token == null)
            return ResponseEntity.badRequest().body("Логин или почта заняты");

        return ResponseEntity.ok(token);
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
        return authService.signIn(request);
    }

    @GetMapping("/confirmEmail")
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
        return authService.confirmEmail(token);
    }

    @PostMapping("/restore")
    public ResponseEntity<?> restoreAccount(@RequestBody SignInRequest request) {
        return userService.restoreUser(userService.getUserByUsername(request.getUsername()));
    }
}
