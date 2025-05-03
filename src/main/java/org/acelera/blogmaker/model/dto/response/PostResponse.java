package org.acelera.blogmaker.model.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String theme,
        Long themeId,
        String name,
        UUID userId,
        String role,
        String userPhoto
) {
}