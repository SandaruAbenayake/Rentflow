package com.rentflow.identity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final UserRepository users;
    private final RefreshTokenRepository refreshTokens;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    private final SecureRandom random = new SecureRandom();

    public AuthenticationController(UserRepository users, RefreshTokenRepository refreshTokens, JwtService jwtService) {
        this.users = users;
        this.refreshTokens = refreshTokens;
        this.jwtService = jwtService;
    }

    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record AuthResponse(String accessToken, String refreshToken, Map<String, Object> user) {}

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        UserAccount user = users.findByEmailIgnoreCase(request.email())
            .filter(UserAccount::isActive)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        String refreshToken = issueRefreshToken(user);
        Map<String, Object> principal = Map.of(
            "id", user.getId().toString(),
            "email", user.getEmail(),
            "fullName", user.getFullName(),
            "role", user.getRole().name(),
            "companyId", user.getCompanyId() == null ? "" : user.getCompanyId().toString()
        );
        return new AuthResponse(jwtService.createAccessToken(user), refreshToken, principal);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody Map<String, String> body) {
        String supplied = body.get("refreshToken");
        if (supplied == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is required");
        String hash = hash(supplied);
        RefreshToken old = refreshTokens.findByTokenHashAndRevokedFalse(hash)
            .filter(token -> token.getExpiresAt().isAfter(Instant.now()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is invalid"));
        old.setRevoked(true);
        refreshTokens.save(old);
        UserAccount user = users.findById(old.getUserId())
            .filter(UserAccount::isActive)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is invalid"));
        return new AuthResponse(jwtService.createAccessToken(user), issueRefreshToken(user), Map.of("id", user.getId().toString()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Boolean>> logout(@RequestBody Map<String, String> body) {
        if (body.get("refreshToken") != null) {
            refreshTokens.findByTokenHashAndRevokedFalse(hash(body.get("refreshToken")))
                .ifPresent(token -> { token.setRevoked(true); refreshTokens.save(token); });
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/internal/company-admin")
    public ResponseEntity<Map<String, String>> provisionCompanyAdmin(
            @RequestHeader("X-Internal-Provisioning-Key") String provisioningKey,
            @RequestBody Map<String, String> body) {
        String expected = System.getenv().getOrDefault("INTERNAL_PROVISIONING_KEY", "change-me-in-production");
        if (!expected.equals(provisioningKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid provisioning key");
        }
        String email = body.get("email");
        String password = body.get("password");
        String fullName = body.get("fullName");
        String companyId = body.get("companyId");
        if (email == null || password == null || fullName == null || companyId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provisioning fields are required");
        }
        if (users.findByEmailIgnoreCase(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        UserAccount user = new UserAccount();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setRole(UserRole.COMPANY_ADMIN);
        user.setCompanyId(UUID.fromString(companyId));
        user.setActive(true);
        users.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("userId", user.getId().toString()));
    }

    private String issueRefreshToken(UserAccount user) {
        byte[] bytes = new byte[48];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        RefreshToken stored = new RefreshToken();
        stored.setId(UUID.randomUUID());
        stored.setUserId(user.getId());
        stored.setTokenHash(hash(token));
        stored.setExpiresAt(Instant.now().plusSeconds(60 * 60 * 24 * 14));
        refreshTokens.save(stored);
        return token;
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to hash refresh token");
        }
    }
}
