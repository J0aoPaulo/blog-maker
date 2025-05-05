package org.acelera.blogmaker.model.dto.response;

import org.acelera.blogmaker.model.User;

import java.util.UUID;

public record UserDto(UUID id, String name, String email, String role, String photo) {
    public UserDto(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole().name(), user.getPhoto());
    }
} 