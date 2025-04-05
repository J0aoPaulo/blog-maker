package org.acelera.blogmaker.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateThemeRequest(
        @NotBlank (message = "{required.description}")
        String description
) {
}