package com.victor.spring_security_project.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotEmpty(message = "Username is needed for registration") // from validation package
        @NotBlank(message = "Username is needed for registration")
        String username,

        @Size(min = 8, message = "Password should be 8 characters or more")
        @NotEmpty(message = "Password is needed for registration") // from validation package
        @NotBlank(message = "Password is needed for registration")
        String password
) {
}
