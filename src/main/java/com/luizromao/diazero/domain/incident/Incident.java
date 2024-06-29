package com.luizromao.diazero.domain.incident;

import lombok.EqualsAndHashCode;

import java.util.List;

import com.luizromao.diazero.domain.incident.dto.CreateIncidentDTO;
import com.luizromao.diazero.domain.incident.dto.DataUpdateIncidentDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "incidents")
@Entity(name = "Incident")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idIncident")
public class Incident {

    public Incident(CreateIncidentDTO data){
        this.name = data.name();
        this.description = data.description();
        this.priority = data.priority();
    }

    public void updateIncident(DataUpdateIncidentDTO data){
        if(data.priority() != null){
            this.priority = data.priority();
        }
    }

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIncident;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name= "priority", nullable = false)
    private IncidentPrioriyType priority;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL)
    private List<IncidentEvent> events;
}
