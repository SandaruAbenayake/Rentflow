package com.rentflow.fleet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface VehicleRepository extends JpaRepository<Vehicle,UUID> {
    List<Vehicle> findByCompanyId(UUID companyId);
    Optional<Vehicle> findByIdAndCompanyId(UUID id, UUID companyId);
    Optional<Vehicle> findByCompanyIdAndRegistrationNumberIgnoreCase(UUID companyId,String registrationNumber);
}
