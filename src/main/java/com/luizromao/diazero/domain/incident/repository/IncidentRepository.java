package com.luizromao.diazero.domain.incident.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luizromao.diazero.domain.incident.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long>{

        @Query("SELECT i FROM Incident i JOIN FETCH i.events e " +
                "ORDER BY " +
                "CASE i.priority " +
                "WHEN 'CRITICAL' THEN 1 " +
                "WHEN 'HIGH' THEN 2 " +
                "WHEN 'MEDIUM' THEN 3 " +
                "WHEN 'LOW' THEN 4 " +
                "END, e.eventDate")
        List<Incident> findAllIncidentsWithEventsOrdered();

        @Query("SELECT i FROM Incident i JOIN FETCH i.events e " +
                "WHERE i.idIncident = :id " +
                "ORDER BY " +
                "CASE i.priority " +
                "WHEN 'CRITICAL' THEN 1 " +
                "WHEN 'HIGH' THEN 2 " +
                "WHEN 'MEDIUM' THEN 3 " +
                "WHEN 'LOW' THEN 4 " +
                "END, e.eventDate")
        Optional<Incident> findIncidentsByIdWithEventsOrdered(@Param("id") Long id);

        @Query(value = "SELECT inc.*, " +
                "inEvents.* " +
                "FROM incident_events inEvents " +
                "INNER JOIN incidents inc ON inEvents.incident_id = inc.id_incident " +
                "ORDER BY inEvents.event_date DESC " +
                "LIMIT 20", nativeQuery= true)
        List<Incident> findTop20ByOrderByDateAtDesc();
}
