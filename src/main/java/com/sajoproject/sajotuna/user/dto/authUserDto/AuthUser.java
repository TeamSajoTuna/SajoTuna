package com.sajoproject.sajotuna.user.dto.authUserDto;

import lombok.Getter;

@Getter
public class AuthUser {
    private final Long id;
    private final String email;
    private final String userRole;

    public AuthUser(Long id, String email, String role) {
        this.id = id;
        this.email = email;
        this.userRole=role;
    }
}
