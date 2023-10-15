package com.relexintern.entrancetest.service;


import com.relexintern.entrancetest.models.EmailConfirmationToken;
import com.relexintern.entrancetest.models.User;
import com.relexintern.entrancetest.repository.EmailConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailConfirmationTokenRepository tokenRepository;

    @Async
    public void sendMessage(String recipient, String subject, String body) {
        var message = new SimpleMailMessage();

        message.setFrom("testapiverification@rambler.ru");
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    @Async
    public void sendConfirmationMail(User user) {
        var token = new EmailConfirmationToken(user);

        var check = tokenRepository.findByUser(user);

        if (check != null)
            tokenRepository.delete(check);

        tokenRepository.save(token);
        sendMessage(user.getEmail(), "Подтвердите почту", "По ссылке " + "http://localhost:8080/confirmEmail?token=" + token.getConfirmationToken());
    }
}
