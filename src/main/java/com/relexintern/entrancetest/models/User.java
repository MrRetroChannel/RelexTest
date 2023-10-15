package com.relexintern.entrancetest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Getter
    @Setter
    private String email;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String surname;

    @Setter
    private String username;

    @Size(min = 4, message = "Password must be not less than 5 characters")
    @Setter
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @Setter
    private Set<Role> roles;

    @ManyToMany
    @Getter
    private List<User> friends;

    @Column(columnDefinition = "boolean default false")
    @Getter
    @Setter
    @JsonIgnore
    private boolean privateFriends;

    @Column(columnDefinition = "boolean default false")
    @Getter
    @Setter
    @JsonIgnore
    private boolean onlyFriendsMessaging;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !roles.contains(new Role(RoleType.DELETED));
    }

    @Override
    public boolean isAccountNonLocked() {
        return !roles.contains(new Role(RoleType.DELETED));
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !roles.contains(new Role(RoleType.DELETED));
    }

    public void addRole(RoleType role) {
        if (this.roles != null)
            this.roles.add(new Role(role));
        else
            this.roles = Collections.singleton(new Role(role));
    }

    public void removeRole(RoleType role) {
        this.roles.remove(new Role(role));
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }

    public boolean isFriend(User friend) {
        return friends.contains(friend);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof User))
            return false;

        return this.getUsername().equals(((User)o).getUsername());
    }
}
