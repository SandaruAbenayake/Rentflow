package com.rentflow.identity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BootstrapRunner implements CommandLineRunner {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Value("${app.seed.demo-data:false}") private boolean seedDemoData;
    @Value("${DEMO_SEED_PASSWORD:}") private String password;

    public BootstrapRunner(UserRepository users) {
        this.users = users;
    }

    @Override
    public void run(String... args) {
        if (!seedDemoData) return;
        if (password.isBlank()) throw new IllegalStateException("DEMO_SEED_PASSWORD is required when demo data seeding is enabled");
        seedUser("admin@acme-rentals.test", "Acme Admin", UserRole.COMPANY_ADMIN,
            UUID.fromString("00000000-0000-0000-0000-000000000001"), "00000000-0000-0000-0000-000000000003");
        seedUser("admin@zenith-motors.test", "Zenith Admin", UserRole.COMPANY_ADMIN,
            UUID.fromString("00000000-0000-0000-0000-000000000002"), "00000000-0000-0000-0000-000000000004");
        seedUser("superadmin@rentflow.test", "Platform Super Admin", UserRole.SUPER_ADMIN,
            null, "00000000-0000-0000-0000-000000000005");
    }

    private void seedUser(String email, String name, UserRole role, UUID companyId, String id) {
        if (users.findByEmailIgnoreCase(email).isPresent()) return;
        UserAccount user = new UserAccount();
        user.setId(UUID.fromString(id));
        user.setEmail(email);
        user.setFullName(name);
        user.setPasswordHash(encoder.encode(password));
        user.setRole(role);
        user.setCompanyId(companyId);
        user.setActive(true);
        users.save(user);
    }
}
