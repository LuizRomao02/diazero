package com.luizromao.diazero.domain.incident.dto;

import com.luizromao.diazero.domain.incident.IncidentEventType;
import com.luizromao.diazero.domain.incident.IncidentPrioriyType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DataUpdateIncidentDTO(
    IncidentPrioriyType priority,
    @NotNull(message = "Event cannot be null")
    IncidentEventType eventType,
    @NotBlank(message = "Event Description cannot be null")
    String eventDescription,
    @NotNull
    Long userBy
) { }
