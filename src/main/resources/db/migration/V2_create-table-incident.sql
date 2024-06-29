CREATE TABLE incidents (
    idIncident BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NOT NULL
);

CREATE TABLE incident_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    incident_id BIGINT,
    event_type ENUM('CREATED', 'UPDATED', 'CLOSED', 'DELETED') NOT NULL,
    event_description TEXT,
    event_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_by BIGINT,
    FOREIGN KEY (incident_id) REFERENCES incidents(id)
    FOREIGN KEY (user_by) REFERENCES users(id) 
);
