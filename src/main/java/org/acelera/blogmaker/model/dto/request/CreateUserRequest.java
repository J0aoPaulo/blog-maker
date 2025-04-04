package org.acelera.blogmaker.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest (
        @NotBlank(message = "{required.email}")
        @Email
        String email,
        @NotBlank(message = "{required.password}")
        String password
){}