package com.relexintern.entrancetest.repository;

import com.relexintern.entrancetest.models.Role;
import com.relexintern.entrancetest.models.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleType> {
}
