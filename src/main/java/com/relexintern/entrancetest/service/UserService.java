package com.relexintern.entrancetest.service;

import com.relexintern.entrancetest.models.DeletedUser;
import com.relexintern.entrancetest.models.Role;
import com.relexintern.entrancetest.models.RoleType;
import com.relexintern.entrancetest.models.User;
import com.relexintern.entrancetest.repository.DeletedUserRepository;
import com.relexintern.entrancetest.repository.RoleRepository;
import com.relexintern.entrancetest.repository.UserRepository;
import org.hibernate.sql.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeletedUserRepository deletedUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);

        if (user == null)
            user = userRepository.findByEmail(username);

        if (user == null)
            throw new UsernameNotFoundException(String.format("User with name %s not found", username));

        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        return getUserByEmail(email) != null;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public boolean addUser(User user) {
        if (usernameExists(user.getUsername()) || emailExists(user.getEmail()))
            return false;

        user.setRoles(Collections.singleton(new Role(RoleType.USER)));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

    public void setAdmin(User user) {
        var newUser = userRepository.findByUsername(user.getUsername());
        if (newUser != null) {
            newUser.addRole(RoleType.ADMIN);
            userRepository.save(newUser);
        }
    }

    public void editUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        user.addRole(RoleType.DELETED);

        deletedUserRepository.save(new DeletedUser(user));
    }

    public ResponseEntity<?> restoreUser(User user) {
        var deletedUser = deletedUserRepository.findByUser(user);
        if (deletedUser != null) {
            deletedUserRepository.delete(deletedUser);

            if (new Date().before(deletedUser.getExpiryTime())) {
                user.removeRole(RoleType.DELETED);
                userRepository.save(user);
                return ResponseEntity.ok("Аккаунт успешно восстановлен");
            }

            userRepository.delete(user);

            return ResponseEntity.badRequest().body("Истек срок на восстановление аккаунта");
        }

        return ResponseEntity.badRequest().body("Удаленного пользователя не существует");
    }
}
