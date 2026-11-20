package com.rentflow.fleet;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.*;
@RestController
@RequestMapping("/api/v1")
public class FleetController {
    private final VehicleRepository vehicles;
    private final DriverRepository drivers;
    public FleetController(VehicleRepository vehicles,DriverRepository drivers){this.vehicles=vehicles;this.drivers=drivers;}
    private UUID company(Authentication auth){
        String value=(String)auth.getDetails();
        if(value==null||value.isBlank()) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Company membership required");
        return UUID.fromString(value);
    }
    public record VehicleRequest(@NotBlank String registrationNumber,@NotBlank String make,@NotBlank String model,
        Integer year,String color,UUID categoryId,String transmission,String fuelType,Integer seatingCapacity,
        @DecimalMin("0.01") BigDecimal dailyRate,String description,String imageUrl,VehicleStatus status){}
    @GetMapping("/vehicles")
    public List<Vehicle> vehicles(Authentication auth){return vehicles.findByCompanyId(company(auth));}
    @PostMapping("/vehicles") @Transactional
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody VehicleRequest request,Authentication auth){
        UUID companyId=company(auth);
        if(vehicles.findByCompanyIdAndRegistrationNumberIgnoreCase(companyId,request.registrationNumber()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Registration number already exists");
        Vehicle vehicle=new Vehicle(); vehicle.setId(UUID.randomUUID()); vehicle.setCompanyId(companyId);
        vehicle.setRegistrationNumber(request.registrationNumber().trim()); vehicle.setMake(request.make());
        vehicle.setModel(request.model()); vehicle.setYear(request.year()); vehicle.setColor(request.color());
        vehicle.setCategoryId(request.categoryId()); vehicle.setTransmission(request.transmission());
        vehicle.setFuelType(request.fuelType()); vehicle.setSeatingCapacity(request.seatingCapacity());
        vehicle.setDailyRate(request.dailyRate()); vehicle.setDescription(request.description());
        vehicle.setImageUrl(request.imageUrl()); vehicle.setStatus(request.status()==null?VehicleStatus.AVAILABLE:request.status());
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicles.save(vehicle));
    }
    @PutMapping("/vehicles/{id}")
    public Vehicle updateVehicle(@PathVariable UUID id,@Valid @RequestBody VehicleRequest request,Authentication auth){
        Vehicle vehicle=vehicles.findByIdAndCompanyId(id,company(auth)).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehicle not found"));
        vehicle.setRegistrationNumber(request.registrationNumber().trim()); vehicle.setMake(request.make()); vehicle.setModel(request.model());
        vehicle.setYear(request.year()); vehicle.setColor(request.color()); vehicle.setTransmission(request.transmission());
        vehicle.setFuelType(request.fuelType()); vehicle.setSeatingCapacity(request.seatingCapacity());
        vehicle.setDailyRate(request.dailyRate()); vehicle.setDescription(request.description()); vehicle.setImageUrl(request.imageUrl());
        if(request.status()!=null) vehicle.setStatus(request.status());
        return vehicles.save(vehicle);
    }
    @DeleteMapping("/vehicles/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable UUID id,Authentication auth){
        Vehicle vehicle=vehicles.findByIdAndCompanyId(id,company(auth)).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehicle not found"));
        vehicles.delete(vehicle);
    }
    public record DriverRequest(@NotBlank String fullName,@NotBlank String phone,String email,@NotBlank String licenseNumber,String status){}
    @GetMapping("/drivers")
    public List<Driver> driverList(Authentication auth){return drivers.findByCompanyId(company(auth));}
    @PostMapping("/drivers")
    public ResponseEntity<Driver> createDriver(@Valid @RequestBody DriverRequest request,Authentication auth){
        UUID companyId=company(auth); Driver driver=new Driver(); driver.setId(UUID.randomUUID()); driver.setCompanyId(companyId);
        driver.setFullName(request.fullName()); driver.setPhone(request.phone()); driver.setEmail(request.email());
        driver.setLicenseNumber(request.licenseNumber()); driver.setStatus(request.status()==null?"AVAILABLE":request.status());
        return ResponseEntity.status(HttpStatus.CREATED).body(drivers.save(driver));
    }
}
