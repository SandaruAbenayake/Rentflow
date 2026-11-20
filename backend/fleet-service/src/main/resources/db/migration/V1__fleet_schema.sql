CREATE TABLE vehicle_categories (
    id CHAR(36) PRIMARY KEY,
    company_id CHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    UNIQUE KEY uk_category_company_name(company_id, name)
) ENGINE=InnoDB;

CREATE TABLE vehicles (
    id CHAR(36) PRIMARY KEY,
    company_id CHAR(36) NOT NULL,
    registration_number VARCHAR(64) NOT NULL,
    make VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year SMALLINT,
    color VARCHAR(50),
    category_id CHAR(36),
    transmission VARCHAR(32),
    fuel_type VARCHAR(32),
    seating_capacity SMALLINT,
    daily_rate DECIMAL(12,2) NOT NULL,
    description TEXT,
    image_url VARCHAR(1000),
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    UNIQUE KEY uk_vehicle_company_registration(company_id, registration_number),
    INDEX idx_vehicle_company_status(company_id, status)
) ENGINE=InnoDB;

CREATE TABLE drivers (
    id CHAR(36) PRIMARY KEY,
    company_id CHAR(36) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(64) NOT NULL,
    email VARCHAR(255),
    license_number VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    UNIQUE KEY uk_driver_company_license(company_id, license_number),
    INDEX idx_driver_company_status(company_id, status)
) ENGINE=InnoDB;
