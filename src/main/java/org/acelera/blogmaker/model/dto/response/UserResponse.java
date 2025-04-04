package org.acelera.blogmaker.model.dto.response;

public record UserResponse(
    String name,
    String email,
    String role,
   String photo
) {
}
