package com.rentflow.fleet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class DemoDataSeeder implements CommandLineRunner {
    private final VehicleRepository vehicles;
    @Value("${app.seed.demo-data:false}") private boolean enabled;

    public DemoDataSeeder(VehicleRepository vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public void run(String... args) {
        if (!enabled) return;
        seed("00000000-0000-0000-0000-000000000101", "00000000-0000-0000-0000-000000000001",
            "ACME-001", "Toyota", "Corolla", 2022, "45.00");
        seed("00000000-0000-0000-0000-000000000102", "00000000-0000-0000-0000-000000000001",
            "ACME-002", "Honda", "CR-V", 2023, "65.00");
        seed("00000000-0000-0000-0000-000000000103", "00000000-0000-0000-0000-000000000002",
            "ZEN-001", "Ford", "Focus", 2021, "40.00");
    }

    private void seed(String id, String companyId, String registration, String make,
                      String model, int year, String rate) {
        UUID vehicleId = UUID.fromString(id);
        if (vehicles.existsById(vehicleId)) return;
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setCompanyId(UUID.fromString(companyId));
        vehicle.setRegistrationNumber(registration);
        vehicle.setMake(make);
        vehicle.setModel(model);
        vehicle.setYear(year);
        vehicle.setDailyRate(new BigDecimal(rate));
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicles.save(vehicle);
    }
}
