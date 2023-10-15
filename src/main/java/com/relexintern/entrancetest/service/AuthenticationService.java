package com.relexintern.entrancetest.service;

import com.relexintern.entrancetest.models.RoleType;
import com.relexintern.entrancetest.models.User;
import com.relexintern.entrancetest.repository.EmailConfirmationTokenRepository;
import com.relexintern.entrancetest.repository.UserRepository;
import com.relexintern.entrancetest.requests.JwtAuthenticationResponse;
import com.relexintern.entrancetest.requests.SignInRequest;
import com.relexintern.entrancetest.requests.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    EmailService emailService;

    @Autowired
    EmailConfirmationTokenRepository tokenRepository;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        User user = new User();

        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        if (userService.addUser(user)) {
            emailService.sendConfirmationMail(user);

            var jwt = jwtService.generateToken(user);
            return JwtAuthenticationResponse.builder().token(jwt).build();
        }

        return null;
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        var user = userRepository.findByUsername(request.getUsername());

        if (user == null)
            user = userRepository.findByEmail(request.getUsername());

        var jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public ResponseEntity<?> confirmEmail(String token) {
        var emailToken = tokenRepository.findByConfirmationToken(token);

        if (emailToken != null) {
            tokenRepository.delete(emailToken);

            if (new Date().before(emailToken.getExpiryTime())) {
                var user = emailToken.getUser();
                user.addRole(RoleType.CONFIRMED);
                userService.editUser(user);
                return ResponseEntity.ok("Почта подтверждена");
            }

            return ResponseEntity.badRequest().body("Время на подтверждение истекло");
        }

        return ResponseEntity.badRequest().body("Неверный код подтверждения");
    }
}
