package com.relexintern.entrancetest.configs;

import com.relexintern.entrancetest.models.Role;
import com.relexintern.entrancetest.models.RoleType;
import com.relexintern.entrancetest.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class StartupDbConfig {
    @Autowired
    RoleRepository roleService;

    @Bean
    public void insertAllRoles() {
        for (var i : RoleType.values())
            if (i.ordinal() != 0)
                roleService.save(new Role(i));
    }
}
