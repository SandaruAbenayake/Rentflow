package com.rentflow.rental;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final SecretKey key; private final String issuer; private final String audience;
    public JwtAuthenticationFilter(@Value("${rentflow.jwt.secret}") String secret,@Value("${rentflow.jwt.issuer}")String issuer,@Value("${rentflow.jwt.audience}")String audience){
        key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.issuer=issuer;this.audience=audience;
    }
    @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException{
        String header=request.getHeader("Authorization");
        if(header!=null&&header.startsWith("Bearer ")){
            try{
                Claims c=Jwts.parser().verifyWith(key).requireIssuer(issuer).requireAudience(audience).build().parseSignedClaims(header.substring(7)).getPayload();
                String role=(String)c.get("role");String company=(String)c.get("companyId");
                var auth=new UsernamePasswordAuthenticationToken(c.getSubject(),null,role==null?List.of():List.of(new SimpleGrantedAuthority("ROLE_"+role)));
                auth.setDetails(company);SecurityContextHolder.getContext().setAuthentication(auth);
            }catch(Exception e){response.setStatus(401);return;}
        }
        chain.doFilter(request,response);
    }
}
