package com.rentflow.company;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
    private final CompanyRepository companies;
    private final ProvisioningSagaRepository sagas;
    private final RestTemplate restTemplate = new RestTemplate();

    public CompanyController(CompanyRepository companies, ProvisioningSagaRepository sagas) {
        this.companies=companies; this.sagas=sagas;
    }

    public record CreateCompanyRequest(@NotBlank @Size(max=255) String name,
                                       @NotBlank @Pattern(regexp="[A-Za-z0-9_-]{2,64}") String code,
                                       @Email String email, @Size(max=64) String phone,
                                       @Size(max=500) String address,
                                       @Email @NotBlank String adminEmail,
                                       @Size(min=8,max=128) String adminPassword,
                                       @NotBlank String adminFullName) {}

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public List<Company> list() { return companies.findAll(); }

    @PostMapping
    @Transactional
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Company> create(@Valid @RequestBody CreateCompanyRequest request) {
        if (companies.findByCodeIgnoreCase(request.code()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Company code already exists");
        Company company=new Company();
        company.setId(UUID.randomUUID()); company.setName(request.name()); company.setCode(request.code().toUpperCase());
        company.setEmail(request.email()); company.setPhone(request.phone()); company.setAddress(request.address());
        company.setStatus(CompanyStatus.PENDING);
        companies.save(company);
        ProvisioningSaga saga=new ProvisioningSaga();
        saga.setId(UUID.randomUUID()); saga.setCompanyId(company.getId()); saga.setState("ADMIN_PROVISIONING_REQUESTED");
        saga.setAttempts(1);
        sagas.save(saga);
        try {
            var identityUrl = System.getenv().getOrDefault("IDENTITY_SERVICE_URL", "http://localhost:8081");
            var provisioningKey = System.getenv().getOrDefault("INTERNAL_PROVISIONING_KEY", "change-me-in-production");
            var headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.set("X-Internal-Provisioning-Key", provisioningKey);
            var body = Map.of(
                "email", request.adminEmail(),
                "password", request.adminPassword(),
                "fullName", request.adminFullName(),
                "companyId", company.getId().toString()
            );
            var response = restTemplate.exchange(identityUrl + "/api/v1/auth/internal/company-admin",
                org.springframework.http.HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(body, headers),
                Map.class);
            if (!response.getStatusCode().is2xxSuccessful()) throw new RestClientException("Identity provisioning failed");
            company.setStatus(CompanyStatus.ACTIVE);
            companies.save(company);
            saga.setState("COMPLETED");
            sagas.save(saga);
        } catch (RestClientException exception) {
            company.setStatus(CompanyStatus.PENDING);
            companies.save(company);
            saga.setAttempts(saga.getAttempts() + 1);
            saga.setState("FAILED");
            saga.setLastError("Identity provisioning failed");
            sagas.save(saga);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "Company created but initial administrator provisioning failed");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public Company setStatus(@PathVariable UUID id, @RequestBody Map<String, Boolean> body) {
        Company company=companies.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Company not found"));
        company.setStatus(Boolean.TRUE.equals(body.get("active")) ? CompanyStatus.ACTIVE : CompanyStatus.INACTIVE);
        return companies.save(company);
    }
}
