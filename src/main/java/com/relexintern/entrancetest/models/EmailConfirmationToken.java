package com.relexintern.entrancetest.models;

import com.relexintern.entrancetest.configs.MailConfig;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class EmailConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private long TokenId;

    @Getter
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private Date expiryTime;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    @Getter
    private User user;

    public EmailConfirmationToken(User user) {
        this.user = user;
        var promDate = Calendar.getInstance();
        promDate.add(Calendar.MINUTE, MailConfig.EXPIRE_TIME_MINUTES);
        expiryTime = promDate.getTime();
        confirmationToken = UUID.randomUUID().toString();
    }
}
