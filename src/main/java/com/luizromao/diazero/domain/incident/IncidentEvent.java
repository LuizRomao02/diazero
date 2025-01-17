package com.luizromao.diazero.domain.incident;

import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luizromao.diazero.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "incident_events")
@Entity(name = "IncidentEvent")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncidentEvent {

    public IncidentEvent(Incident incident, IncidentEventType eventType, String eventDescription, LocalDateTime eventDate, User userBy) {
        this.incident = incident;
        this.eventType = eventType;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.userBy = userBy;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id")
    @JsonIgnore
    private Incident incident;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private IncidentEventType eventType;

    @Column(name = "event_description", columnDefinition = "TEXT")
    private String eventDescription;

    @Column(name = "event_date", nullable = false)
    @EqualsAndHashCode.Exclude
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "user_by")
    private User userBy;

}
