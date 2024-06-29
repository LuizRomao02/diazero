package com.luizromao.diazero.domain.incident.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizromao.diazero.domain.incident.Incident;
import com.luizromao.diazero.domain.incident.IncidentEvent;
import com.luizromao.diazero.domain.incident.IncidentEventType;
import com.luizromao.diazero.domain.incident.dto.CreateIncidentDTO;
import com.luizromao.diazero.domain.incident.dto.DataUpdateIncidentDTO;
import com.luizromao.diazero.domain.incident.repository.IncidentEventRepository;
import com.luizromao.diazero.domain.incident.repository.IncidentRepository;
import com.luizromao.diazero.domain.user.User;
import com.luizromao.diazero.infra.exception.IncidentException;

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

    public List<Incident> getIncidentsWithEvents() {
        return incidentRepository.findAllIncidentsWithEventsOrdered();
    }

    public void updateIncidentById(Long id, @Valid DataUpdateIncidentDTO dto) {
        Incident incident = incidentRepository.getReferenceById(id);
        
        for (IncidentEvent events : incident.getEvents()) {
            if(events.getEventType() == IncidentEventType.CLOSED || events.getEventType() == IncidentEventType.DELETED){
                throw new IncidentException("It is not possible to change this incident");
            }
        }

        incident.updateIncident(dto);

        User userBy = new User();
        userBy.setId(dto.userBy());

        IncidentEvent incidentEvent = new IncidentEvent(
                incident,
                dto.eventType(),
                dto.eventDescription(),
                PROCESSING_DATE,
                userBy
        );
        eventRepository.save(incidentEvent);
    }

}
