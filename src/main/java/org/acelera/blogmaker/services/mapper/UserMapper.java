package org.acelera.blogmaker.services.mapper;

import org.acelera.blogmaker.model.Role;
import org.acelera.blogmaker.model.User;
import org.acelera.blogmaker.model.dto.request.CreateUserRequest;
import org.acelera.blogmaker.model.dto.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public User toUser(CreateUserRequest request, Role role) {
        return User.builder()
                .id(null)
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .role(role)
                .build();
    }

    public UserResponse fromUser(User user) {
       return new UserResponse(
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getPhoto()
       );
    }
}
