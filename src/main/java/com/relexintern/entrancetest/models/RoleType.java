package com.relexintern.entrancetest.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleType {
    NONE("NONE"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    DELETED("ROLE_DELETED"),
    CONFIRMED("ROLE_CONFIRMED");

    private final String role;

    RoleType(String role) {
        this.role = role;
    }

    @JsonValue
    public String getRole() {
        return role;
    }
}
