package com.luizromao.diazero.domain.incident.dto;

import jakarta.validation.constraints.NotNull;

public record DataDeleteIncidentDTO (
    @NotNull
    Long userBy
) {}