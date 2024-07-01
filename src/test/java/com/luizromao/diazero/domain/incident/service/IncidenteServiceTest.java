package com.luizromao.diazero.domain.incident.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.luizromao.diazero.DiazeroApplication;
import com.luizromao.diazero.domain.incident.Incident;
import com.luizromao.diazero.domain.incident.IncidentEvent;
import com.luizromao.diazero.domain.incident.IncidentEventType;
import com.luizromao.diazero.domain.incident.IncidentPrioriyType;
import com.luizromao.diazero.domain.incident.dto.CreateIncidentDTO;
import com.luizromao.diazero.domain.incident.dto.DataUpdateIncidentDTO;
import com.luizromao.diazero.domain.incident.repository.IncidentEventRepository;
import com.luizromao.diazero.domain.incident.repository.IncidentRepository;
import com.luizromao.diazero.infra.exception.IncidentException;

@SpringBootTest(classes = DiazeroApplication.class)
@ExtendWith(MockitoExtension.class)
class IncidenteServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private IncidentEventRepository eventRepository;

    @InjectMocks
    private IncidentService incidentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create the incident with the http status 200")
    void createNewIncident200Test(){
        CreateIncidentDTO incidentDto = new CreateIncidentDTO("Test Incident", "Test Description", IncidentPrioriyType.HIGH, 1L);

        Incident mockedIncident = new Incident();
        mockedIncident.setName("Test Incident");
        mockedIncident.setDescription("Test Description");
        mockedIncident.setPriority(IncidentPrioriyType.HIGH);

        when(incidentRepository.save(any(Incident.class))).thenReturn(mockedIncident);

        IncidentEvent mockedEvent = new IncidentEvent();
        when(eventRepository.save(any(IncidentEvent.class))).thenReturn(mockedEvent);

        Incident result = incidentService.createNewIncident(incidentDto);
        
        verify(incidentRepository, times(1)).save(any(Incident.class));
        verify(eventRepository, times(1)).save(any(IncidentEvent.class));

        assertEquals(mockedIncident.getName(), result.getName());
        assertEquals(mockedIncident.getDescription(), result.getDescription());
        assertEquals(mockedIncident.getPriority(), result.getPriority());
    }

    @Test
    @DisplayName("Should return incidents with events with the http status 200")
    void getIncidentsWithEvents200Test(){
        Incident incident1 = new Incident();
        incident1.setIdIncident(1L);
        Incident incident2 = new Incident();
        incident2.setIdIncident(2L);

        List<Incident> expectedIncidents = Arrays.asList(incident2, incident1);

        when(incidentRepository.findAllIncidentsWithEventsOrdered()).thenReturn(expectedIncidents);

        List<Incident> result = incidentService.getIncidentsWithEvents();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(expectedIncidents.size());
        assertThat(result.get(0).getIdIncident()).isEqualTo(2L);
        assertThat(result.get(1).getIdIncident()).isEqualTo(1L);

        verify(incidentRepository, times(1)).findAllIncidentsWithEventsOrdered();
    }
    
    @Test
    @DisplayName("Should return latest incidents with events with the http status 200")
    void findLatestIncidents200Test(){
        Incident incident1 = new Incident();
        incident1.setIdIncident(1L);

        Incident incident2 = new Incident();
        incident2.setIdIncident(2L);

        List<Incident> expectedIncidents = new ArrayList<>();
        expectedIncidents.add(incident1);
        expectedIncidents.add(incident2);

        when(incidentRepository.findTop20ByOrderByDateAtDesc()).thenReturn(expectedIncidents);

        List<Incident> result = incidentService.findLatestIncidents();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(expectedIncidents.size());
        assertThat(result.get(0).getIdIncident()).isEqualTo(1L); 
        assertThat(result.get(1).getIdIncident()).isEqualTo(2L);

        verify(incidentRepository, times(1)).findTop20ByOrderByDateAtDesc();
    }

    @Test
    @DisplayName("Should return incidents by id with events with the http status 200")
    void getIncidentById200Test() {
        Long id = 1L;
        Incident incident = new Incident();
        incident.setIdIncident(id);

        when(incidentRepository.findIncidentsByIdWithEventsOrdered(id)).thenReturn(Optional.of(incident));

        Optional<Incident> resultOptional = incidentService.getIncidentById(id);

        assertThat(resultOptional).isPresent();
        Incident result = resultOptional.get();
        assertThat(result.getIdIncident()).isEqualTo(id);

        verify(incidentRepository, times(1)).findIncidentsByIdWithEventsOrdered(id);
    }

    @Test
    @DisplayName("Should return incidents by id with events with the http status 400")
    void getIncidentById404Test() {
        Long id = 1L;

        when(incidentRepository.findIncidentsByIdWithEventsOrdered(id)).thenReturn(Optional.empty());

        Optional<Incident> resultOptional = incidentService.getIncidentById(id);

        assertThat(resultOptional).isEmpty(); 

        verify(incidentRepository, times(1)).findIncidentsByIdWithEventsOrdered(id);
    }

    @Test
    @DisplayName("Should update the incident by id with the http status 200")
    void updateIncidentById200Test() {
        Long id = 1L;
        DataUpdateIncidentDTO dto = new DataUpdateIncidentDTO(IncidentPrioriyType.LOW, IncidentEventType.CLOSED, "Test description", 1L);
    
        IncidentEvent incidentEvent = new IncidentEvent();
        incidentEvent.setId(1L);

        List<IncidentEvent> listEvent = new ArrayList<>();
        listEvent.add(incidentEvent);

        Incident incident = new Incident();
        incident.setIdIncident(id);
        incident.setEvents(listEvent);
    
        when(incidentRepository.getReferenceById(id)).thenReturn(incident);
    
        assertDoesNotThrow(() -> incidentService.updateIncidentById(id, dto));
    }

    @Test
    @DisplayName("If the incident type Create, return HTTP status 400")
    void updateIncidentByIdCreatedEventType400Test() {
        Long id = 1L;
        DataUpdateIncidentDTO dto = new DataUpdateIncidentDTO(IncidentPrioriyType.HIGH, IncidentEventType.CREATED, "Test description", 1L);

        IncidentException exception = assertThrows(IncidentException.class, () -> incidentService.updateIncidentById(id, dto));

        assertThat(exception.getMessage()).isEqualTo("Cannot update incident with CREATED event type.");
    }

    @Test
    @DisplayName("If the incident type Deleted, return HTTP status 400")
    void updateIncidentByIdDeletedEventType400Test() {
        Long id = 1L;
        DataUpdateIncidentDTO dto = new DataUpdateIncidentDTO(IncidentPrioriyType.HIGH, IncidentEventType.DELETED, "Test description", 1L);

        IncidentException exception = assertThrows(IncidentException.class, () -> incidentService.updateIncidentById(id, dto));

        assertThat(exception.getMessage()).isEqualTo("It is not possible to perform this operation.");
    }

    @Test
    @DisplayName("If the incident event has been closed or deleted, return HTTP status 400")
    void updateIncidentByIdClosedOrDeletedEvent400Test() {
        Long id = 1L;
        DataUpdateIncidentDTO dto = new DataUpdateIncidentDTO(IncidentPrioriyType.LOW, IncidentEventType.CLOSED, "Test description", 1L);
    
        IncidentEvent incidentEvent = new IncidentEvent();
        incidentEvent.setId(1L);
        incidentEvent.setEventType(IncidentEventType.DELETED);

        List<IncidentEvent> listEvent = new ArrayList<>();
        listEvent.add(incidentEvent);

        Incident incident = new Incident();
        incident.setIdIncident(id);
        incident.setEvents(listEvent);
    
        when(incidentRepository.getReferenceById(id)).thenReturn(incident);
    
        IncidentException exception = assertThrows(IncidentException.class, () -> incidentService.updateIncidentById(id, dto));

        assertThat(exception.getMessage()).isEqualTo("It is not possible to change this incident.");
    }

    @Test
    @DisplayName("Should create the incident with the http status 200")
    void deleteIncident200Test() {
        Long id = 1L;
        Long idUser = 2L;

        IncidentEvent incidentEvent = new IncidentEvent();
        incidentEvent.setId(1L);

        List<IncidentEvent> listEvent = new ArrayList<>();
        listEvent.add(incidentEvent);

        Incident incident = new Incident();
        incident.setIdIncident(id);
        incident.setEvents(listEvent);
    
        when(incidentRepository.getReferenceById(id)).thenReturn(incident);
    
        assertDoesNotThrow(() -> incidentService.deleteIncident(id, idUser));
    }

    @Test
    @DisplayName("If the incident event has been closed or deleted, return HTTP status 400")
    void deleteIncidentClosedOrDeletedEvent400Test() {
        Long id = 1L;
        Long idUser = 2L;

        IncidentEvent incidentEvent = new IncidentEvent();
        incidentEvent.setId(1L);
        incidentEvent.setEventType(IncidentEventType.CLOSED);

        List<IncidentEvent> listEvent = new ArrayList<>();
        listEvent.add(incidentEvent);

        Incident incident = new Incident();
        incident.setIdIncident(id);
        incident.setEvents(listEvent);
    
        when(incidentRepository.getReferenceById(id)).thenReturn(incident);
    
        IncidentException exception = assertThrows(IncidentException.class, () -> incidentService.deleteIncident(id, idUser));

        assertThat(exception.getMessage()).isEqualTo("Unable to delete this incident!");
    }

}
