CREATE TABLE companies (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(64) NOT NULL UNIQUE,
    email VARCHAR(255),
    phone VARCHAR(64),
    address VARCHAR(500),
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE provisioning_sagas (
    id CHAR(36) PRIMARY KEY,
    company_id CHAR(36) NOT NULL,
    state VARCHAR(32) NOT NULL,
    attempts INT NOT NULL DEFAULT 0,
    last_error VARCHAR(1000),
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
) ENGINE=InnoDB;
