package com.relexintern.entrancetest.repository;

import com.relexintern.entrancetest.models.EmailConfirmationToken;
import com.relexintern.entrancetest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {
    EmailConfirmationToken findByConfirmationToken(String confirmationToken);

    EmailConfirmationToken findByUser(User user);
}
