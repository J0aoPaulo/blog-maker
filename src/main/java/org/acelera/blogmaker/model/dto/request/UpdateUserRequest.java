package org.acelera.blogmaker.model.dto.request;

import org.acelera.blogmaker.model.User;

public record UpdateUserRequest(
        String name,
        String email,
        String password,
        String photo
) {
    public UpdateUserRequest(User user) {
        this(user.getName(), user.getEmail(), user.getPassword(), user.getPhoto());
    }
}
