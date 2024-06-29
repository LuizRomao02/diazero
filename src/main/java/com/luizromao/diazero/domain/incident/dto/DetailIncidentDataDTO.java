package com.luizromao.diazero.domain.incident.dto;

import com.luizromao.diazero.domain.incident.Incident;
import com.luizromao.diazero.domain.incident.IncidentPrioriyType;

public record DetailIncidentDataDTO(Long idIncident, String name, String description, IncidentPrioriyType priority) {

    public DetailIncidentDataDTO(Incident incident){
        this(incident.getIdIncident(), incident.getName(), incident.getDescription(), incident.getPriority());
    }
}
