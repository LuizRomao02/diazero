package com.luizromao.diazero.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.luizromao.diazero.domain.incident.Incident;
import com.luizromao.diazero.domain.incident.dto.CreateIncidentDTO;
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
}
