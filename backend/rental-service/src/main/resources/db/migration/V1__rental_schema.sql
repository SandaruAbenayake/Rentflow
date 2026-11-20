CREATE TABLE customers (
    id CHAR(36) PRIMARY KEY,
    company_id CHAR(36) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(64) NOT NULL,
    address VARCHAR(500),
    identification_type VARCHAR(64),
    identification_number VARCHAR(128),
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    INDEX idx_customer_company(company_id)
) ENGINE=InnoDB;

CREATE TABLE bookings (
    id CHAR(36) PRIMARY KEY,
    company_id CHAR(36) NOT NULL,
    customer_id CHAR(36) NOT NULL,
    vehicle_id CHAR(36) NOT NULL,
    driver_id CHAR(36),
    start_at TIMESTAMP(6) NOT NULL,
    end_at TIMESTAMP(6) NOT NULL,
    daily_rate_snapshot DECIMAL(12,2) NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    deposit_amount DECIMAL(12,2),
    status VARCHAR(32) NOT NULL,
    notes TEXT,
    created_by CHAR(36) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    INDEX idx_booking_overlap(company_id,vehicle_id,status,start_at,end_at)
) ENGINE=InnoDB;

CREATE TABLE payments (
    id CHAR(36) PRIMARY KEY,
    company_id CHAR(36) NOT NULL,
    booking_id CHAR(36) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_method VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL,
    transaction_reference VARCHAR(255),
    payment_date TIMESTAMP(6) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP(6) NOT NULL,
    INDEX idx_payment_company_booking(company_id,booking_id)
) ENGINE=InnoDB;

CREATE TABLE trips (
    id CHAR(36) PRIMARY KEY,
    company_id CHAR(36) NOT NULL,
    booking_id CHAR(36) NOT NULL,
    vehicle_id CHAR(36) NOT NULL,
    driver_id CHAR(36),
    trip_type VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    start_at TIMESTAMP(6),
    end_at TIMESTAMP(6),
    start_location VARCHAR(500),
    end_location VARCHAR(500),
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    INDEX idx_trip_company_booking(company_id,booking_id)
) ENGINE=InnoDB;

CREATE TABLE booking_locks (
    company_id CHAR(36) NOT NULL,
    vehicle_id CHAR(36) NOT NULL,
    PRIMARY KEY (company_id, vehicle_id)
) ENGINE=InnoDB;
