package com.ssafy.enjoytrip.features.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private Uid uid;
    private String id;
    private String name;
    private Role role;
    private Boolean activated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setUserRole() {
        role = Role.ROLE_USER;
    }

    public void setAdminRole() {
        role = Role.ROLE_ADMIN;
    }

    public void activate() {
        activated = true;
    }

    public void setCreateStatus() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        activated = true;
    }

    public void setUpdateStatus() {
        updatedAt = LocalDateTime.now();
    }
}
