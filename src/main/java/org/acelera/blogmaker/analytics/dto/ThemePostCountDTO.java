package org.acelera.blogmaker.analytics.dto;

public record ThemePostCountDTO(Long   themeId,
                                String themeDescription,
                                Long   totalPosts) {}
