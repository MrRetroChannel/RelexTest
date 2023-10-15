package com.relexintern.entrancetest.models;

import com.relexintern.entrancetest.configs.MailConfig;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Entity
@NoArgsConstructor
public class DeletedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "deleted_user_id")
    private Long deletedUserId;

    @OneToOne(targetEntity = User.class)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    private Date expiryTime;

    public DeletedUser(User user) {
        this.user = user;
        var promDate = Calendar.getInstance();
        promDate.add(Calendar.DATE, 1);
        expiryTime = promDate.getTime();
    }
}
