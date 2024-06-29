package com.luizromao.diazero.domain.incident.dto;

import com.luizromao.diazero.domain.incident.IncidentPrioriyType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateIncidentDTO(
    @NotBlank
    String name,
    @NotBlank
    String description,
    @NotNull
    IncidentPrioriyType priority,
    @NotNull
    Long userCreate
) {}
