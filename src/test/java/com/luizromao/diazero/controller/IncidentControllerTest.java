package com.luizromao.diazero.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizromao.diazero.DiazeroApplication;
import com.luizromao.diazero.domain.incident.Incident;
import com.luizromao.diazero.domain.incident.IncidentEventType;
import com.luizromao.diazero.domain.incident.IncidentPrioriyType;
import com.luizromao.diazero.domain.incident.dto.CreateIncidentDTO;
import com.luizromao.diazero.domain.incident.dto.DataUpdateIncidentDTO;
import com.luizromao.diazero.domain.incident.dto.DetailIncidentDataDTO;
import com.luizromao.diazero.domain.incident.service.IncidentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest(classes = DiazeroApplication.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureJsonTesters
class IncidentControllerTest {

    @Mock
    IncidentService service;

    @InjectMocks
    IncidentController incidentController;

    @Autowired
    private JacksonTester<CreateIncidentDTO> dataCreateIncidentJson;

    @Autowired
    private JacksonTester<DetailIncidentDataDTO> detailIncidentDataJson;

    MockMvc mockMvc;

    @BeforeEach
    void setup(){
		mockMvc = MockMvcBuilders.standaloneSetup(incidentController).build();
    }

    @Test
    @DisplayName("Should return http code 200 when information is valid")
    @WithMockUser
    void createIncident200Test() throws Exception {
        var dataIncident = new CreateIncidentDTO(
            "incident", 
            "description", 
            IncidentPrioriyType.CRITICAL, 
            1L
        );

        Incident incident = new Incident();
        incident.setIdIncident(1L);
        incident.setName("incident");
        incident.setDescription("description");
        incident.setPriority(IncidentPrioriyType.CRITICAL);

        when(service.createNewIncident(any(CreateIncidentDTO.class))).thenReturn(incident);

        var response = mockMvc.perform(post("/incident").contentType(MediaType.APPLICATION_JSON)
                    .content(dataCreateIncidentJson.write(dataIncident).getJson())).andReturn().getResponse();

        var detailIncident = new DetailIncidentDataDTO(
            1L, 
            dataIncident.name(), 
            dataIncident.description(), 
            dataIncident.priority()
        );

        var jsonReturn = detailIncidentDataJson.write(detailIncident).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonReturn);
    }

    @Test
    @DisplayName("Should return http code 400 when information is invalid")
    @WithMockUser
    void createIncident400Test() throws Exception{
        var response = mockMvc.perform(post("/incident")).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Should return http code 200 when information is valid")
    @WithMockUser
    void getAllIncidents200Test() throws Exception{
        List<Incident> listIncident = new ArrayList<>();
        when(service.getIncidentsWithEvents()).thenReturn(listIncident);

        mockMvc.perform(get("/incident")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return last 20 incidente with http code 200 when information is valid")
    @WithMockUser
    void getTheLast20Incident200Test() throws Exception{
        List<Incident> listIncident = new ArrayList<>();
        when(service.findLatestIncidents()).thenReturn(listIncident);

        mockMvc.perform(get("/incident/theLastIncident")).andExpect(status().isOk());
    }

    @Test
    @DisplayName("The incident must be returned with http code 200 when the information is valid")
    @WithMockUser
    void getIncidentById200Test() throws Exception{
        Incident incident = new Incident();
        when(service.getIncidentById(1L)).thenReturn(Optional.of(incident));

        mockMvc.perform(get("/incident/{id}", 1L).accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should update the incident by ID with status 200")
    @WithMockUser
    void updateIncident200Test() throws Exception {
        Long incidentId = 1L;
        DataUpdateIncidentDTO updateDto = new DataUpdateIncidentDTO(
            IncidentPrioriyType.CRITICAL,
            IncidentEventType.CLOSED,
            "Description",
            1L
        );

        doNothing().when(service).updateIncidentById(eq(incidentId), any(DataUpdateIncidentDTO.class));

        mockMvc.perform(put("/incident/{id}", incidentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Record updated successfully."));
    }

    @Test
    @DisplayName("Should delete the incident by ID with status 200")
    @WithMockUser
    void deleteIncident200Test() throws Exception {
        Long incidentId = 1L;
        Long userId = 1L;

        doNothing().when(service).deleteIncident(incidentId, userId);

        mockMvc.perform(delete("/incident/{id}/{idUser}", incidentId, userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Incident excluded!"));
    }
}
