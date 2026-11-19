package com.logistics.core.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AgentChatRequestDto(
    @NotBlank(message = "El prompt no puede estar vacío")
    @Size(min = 2, message = "La consulta debe tener al menos 2 caracteres")
    String prompt
) {}