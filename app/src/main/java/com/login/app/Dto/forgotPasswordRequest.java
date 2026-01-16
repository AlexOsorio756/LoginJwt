package com.login.app.Dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Correo electrónico del usuario", example = "usuario@gmail.com", required = true)
public record forgotPasswordRequest(String email) {}