package com.relexintern.entrancetest.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "role")
    @Setter
    private RoleType role;

    @Override
    public String getAuthority() {
        return role.getRole();
    }

    @Override
    public int hashCode() {
        return this.role.ordinal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Role))
            return false;

        return this.getRole() == ((Role)o).getRole();
    }
}
