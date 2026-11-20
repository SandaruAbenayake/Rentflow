package com.rentflow.rental;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TripRepository extends JpaRepository<Trip,UUID> {
    List<Trip> findByCompanyId(UUID companyId);
}
