package com.rentflow.rental;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface PaymentRepository extends JpaRepository<Payment,UUID> {
    List<Payment> findByCompanyId(UUID companyId);
}
