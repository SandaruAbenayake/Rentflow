package com.rentflow.company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ProvisioningSagaRepository extends JpaRepository<ProvisioningSaga, UUID> {}
