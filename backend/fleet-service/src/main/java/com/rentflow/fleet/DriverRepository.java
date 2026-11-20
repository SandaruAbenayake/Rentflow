package com.rentflow.fleet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface DriverRepository extends JpaRepository<Driver,UUID> {
    List<Driver> findByCompanyId(UUID companyId);
    Optional<Driver> findByIdAndCompanyId(UUID id,UUID companyId);
}
