package com.luizromao.diazero.domain.incident.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luizromao.diazero.domain.incident.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long>{

}
