package com.relexintern.entrancetest.repository;

import com.relexintern.entrancetest.models.DeletedUser;
import com.relexintern.entrancetest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeletedUserRepository extends JpaRepository<DeletedUser, Long> {
    DeletedUser findByUser(User user);
}
