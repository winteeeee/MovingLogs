package com.ssafy.enjoytrip.features.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private Uid uid;
    private String id;
    private String pw;
    private String name;
    private String email;
    private Role role;
    private Boolean activated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isValid(String pw, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(pw, this.pw);
    }

    public void changePw(String pw, PasswordEncoder passwordEncoder) {
        this.pw = passwordEncoder.encode(pw);
    }

    public void setUserRole() {
        role = Role.ROLE_USER;
    }

    public void setAdminRole() {
        role = Role.ROLE_ADMIN;
    }

    public void setCreateStatus() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public void setUpdateStatus() {
        updatedAt = LocalDateTime.now();
    }
}
