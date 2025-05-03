package org.acelera.blogmaker.analytics.dto;

import java.util.UUID;

public record AuthorPostCountDTO(UUID userId,
                                 String authorName,
                                 Long   totalPosts) {}
