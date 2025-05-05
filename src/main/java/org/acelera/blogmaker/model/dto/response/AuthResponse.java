package org.acelera.blogmaker.model.dto.response;

public record AuthResponse(String token, UserDto user) {
}