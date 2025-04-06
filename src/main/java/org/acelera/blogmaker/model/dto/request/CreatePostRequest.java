package org.acelera.blogmaker.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePostRequest(
        @NotBlank(message = "{required.title}")
        String title,

        @NotBlank(message = "{required.content}")
        String content,

        Long themeId,

        @NotNull(message = "{required.user}")
        UUID userId
) {
}
