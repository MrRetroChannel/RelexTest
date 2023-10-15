package com.relexintern.entrancetest.repository;

import com.relexintern.entrancetest.models.FriendRequest;
import com.relexintern.entrancetest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    FriendRequest findByFromAndTo(User from, User to);
}
