package org.acelera.blogmaker.model.dto.response;

import java.time.LocalDateTime;

public record PostResponse(
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String theme,
        String name,
        String role
) {
}