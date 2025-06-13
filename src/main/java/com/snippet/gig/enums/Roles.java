package com.snippet.gig.enums;

import com.snippet.gig.exception.BadRequestException;
import lombok.Getter;

@Getter
public enum Roles {
    MANAGER("MANAGER"),
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    Roles(String value) {
        this.value = value;
    }

    public static Roles fromValue(String value) {
        for (Roles role : Roles.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new BadRequestException("Unknown role: " + value);
    }
}
