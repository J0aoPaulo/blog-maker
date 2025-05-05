package org.acelera.blogmaker.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreatePostRequest(
        @NotBlank(message = "{required.title}")
        String title,

        @NotBlank(message = "{required.content}")
        String content,

        Long themeId
) {
}
