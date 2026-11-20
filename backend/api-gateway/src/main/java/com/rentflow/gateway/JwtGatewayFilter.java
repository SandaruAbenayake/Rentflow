package com.rentflow.gateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {
    private final SecretKey key; private final String issuer; private final String audience;
    public JwtGatewayFilter(@Value("${rentflow.jwt.secret}") String secret,
                            @Value("${rentflow.jwt.issuer}")String issuer,
                            @Value("${rentflow.jwt.audience}")String audience){
        key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.issuer=issuer;this.audience=audience;
    }
    @Override public Mono<Void> filter(org.springframework.web.server.ServerWebExchange exchange,
                                       org.springframework.cloud.gateway.filter.GatewayFilterChain chain){
        String path=exchange.getRequest().getURI().getPath();
        if(path.startsWith("/api/v1/auth/")) return chain.filter(exchange);
        String header=exchange.getRequest().getHeaders().getFirst("Authorization");
        if(header==null||!header.startsWith("Bearer ")){exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);return exchange.getResponse().setComplete();}
        try{
            Claims c=Jwts.parser().verifyWith(key).requireIssuer(issuer).requireAudience(audience).build().parseSignedClaims(header.substring(7)).getPayload();
            ServerHttpRequest mutated=exchange.getRequest().mutate().header("X-Correlation-Id",
                exchange.getRequest().getHeaders().getFirst("X-Correlation-Id")==null?UUID.randomUUID().toString():
                exchange.getRequest().getHeaders().getFirst("X-Correlation-Id")).build();
            return chain.filter(exchange.mutate().request(mutated).build());
        }catch(Exception e){exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);return exchange.getResponse().setComplete();}
    }
    @Override public int getOrder(){return -100;}
}
