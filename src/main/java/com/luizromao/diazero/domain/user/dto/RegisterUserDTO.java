package com.luizromao.diazero.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterUserDTO(
    @NotBlank
    String name,
    @NotBlank
    @Email
    String login,
    @NotNull
    String password
) { }
