package com.luizromao.diazero.domain.incident.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizromao.diazero.domain.incident.Incident;
import com.luizromao.diazero.domain.incident.IncidentEvent;
import com.luizromao.diazero.domain.incident.IncidentEventType;
import com.luizromao.diazero.domain.incident.dto.CreateIncidentDTO;
import com.luizromao.diazero.domain.incident.repository.IncidentEventRepository;
import com.luizromao.diazero.domain.incident.repository.IncidentRepository;
import com.luizromao.diazero.domain.user.User;

import jakarta.validation.Valid;

@Service
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentEventRepository eventRepository;

    private static final String INCIDENT_CREATED = "Incident created success!";
    private static final LocalDateTime PROCESSING_DATE = LocalDateTime.now();

    public Incident createNewIncident(@Valid CreateIncidentDTO incidentDto) {
        Incident incident = new Incident(incidentDto);
        incidentRepository.save(incident);

        User userCreated = new User();
        userCreated.setId(incidentDto.userCreate());

        IncidentEvent incidentEvent = new IncidentEvent(
                incident,
                IncidentEventType.CREATED,
                INCIDENT_CREATED,
                PROCESSING_DATE,
                userCreated
        );
        eventRepository.save(incidentEvent);

        return incident;
    }
}
