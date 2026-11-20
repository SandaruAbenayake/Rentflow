package com.rentflow.company;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DemoDataSeeder implements CommandLineRunner {
    private final CompanyRepository companies;
    @Value("${app.seed.demo-data:false}") private boolean enabled;

    public DemoDataSeeder(CompanyRepository companies) {
        this.companies = companies;
    }

    @Override
    public void run(String... args) {
        if (!enabled) return;
        seed("00000000-0000-0000-0000-000000000001", "Acme Rentals", "ACME",
            "admin@acme-rentals.test", "+1-555-0100");
        seed("00000000-0000-0000-0000-000000000002", "Zenith Motors", "ZENITH",
            "admin@zenith-motors.test", "+1-555-0200");
    }

    private void seed(String id, String name, String code, String email, String phone) {
        UUID companyId = UUID.fromString(id);
        if (companies.existsById(companyId)) return;
        Company company = new Company();
        company.setId(companyId);
        company.setName(name);
        company.setCode(code);
        company.setEmail(email);
        company.setPhone(phone);
        company.setStatus(CompanyStatus.ACTIVE);
        companies.save(company);
    }
}
