package com.rentflow.identity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {
    private final SecretKey key;
    private final String issuer;
    private final String audience;

    public JwtService(@Value("${rentflow.jwt.secret}") String secret,
                      @Value("${rentflow.jwt.issuer}") String issuer,
                      @Value("${rentflow.jwt.audience}") String audience) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.audience = audience;
    }

    public String createAccessToken(UserAccount user) {
        Instant now = Instant.now();
        return Jwts.builder()
            .issuer(issuer)
            .audience().add(audience).and()
            .subject(user.getId().toString())
            .claim("email", user.getEmail())
            .claim("role", user.getRole().name())
            .claim("companyId", user.getCompanyId() == null ? "" : user.getCompanyId().toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(900)))
            .signWith(key)
            .compact();
    }
}
