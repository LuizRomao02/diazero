package com.luizromao.diazero.domain.incident.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luizromao.diazero.domain.incident.IncidentEvent;

@Repository
public interface IncidentEventRepository extends JpaRepository<IncidentEvent, Long>{

}
