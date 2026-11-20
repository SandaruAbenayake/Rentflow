package com.rentflow.rental;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CustomerRepository extends JpaRepository<Customer,UUID> {
    List<Customer> findByCompanyId(UUID companyId);
    Optional<Customer> findByIdAndCompanyId(UUID id,UUID companyId);
}
