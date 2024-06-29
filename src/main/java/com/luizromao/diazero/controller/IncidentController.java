package com.luizromao.diazero.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.luizromao.diazero.domain.incident.Incident;
import com.luizromao.diazero.domain.incident.IncidentEventType;
import com.luizromao.diazero.domain.incident.dto.CreateIncidentDTO;
import com.luizromao.diazero.domain.incident.dto.DataUpdateIncidentDTO;
import com.luizromao.diazero.domain.incident.dto.DetailIncidentDataDTO;
import com.luizromao.diazero.domain.incident.service.IncidentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/incident")
@SecurityRequirement(name = "bearer-key")
public class IncidentController {

    @Autowired
    private IncidentService service;

    @PostMapping
    @Transactional
    public ResponseEntity<?> createIncident(@RequestBody @Valid CreateIncidentDTO incidentDto, UriComponentsBuilder uriBuilder){
        Incident incident = service.createNewIncident(incidentDto);
        var uri = uriBuilder.path("/incident/{id}").buildAndExpand(incident.getIdIncident()).toUri();

        return ResponseEntity.created(uri).body(new DetailIncidentDataDTO(incident));
    }

    @GetMapping
    public ResponseEntity<?> getAllIncidents(){
        List<Incident> incidents = service.getIncidentsWithEvents();
        return ResponseEntity.ok(incidents);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> updateIncident(@PathVariable Long id, @RequestBody @Valid DataUpdateIncidentDTO dto) {
        if(dto.eventType() == IncidentEventType.CREATED){
            return ResponseEntity.badRequest().body("Cannot update incident with CREATED event type.");
        }
        service.updateIncidentById(id, dto);

        return ResponseEntity.ok("Record updated successfully.");
    }
}
