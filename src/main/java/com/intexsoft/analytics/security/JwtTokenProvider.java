package com.intexsoft.analytics.security;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final Long JWT_VALIDITY_MILLISECONDS = 3600000L;

    private static final String ROLE_CLAIM = "role";

    private static final String DEPARTMENT_CLAIM = "departmentId";

    @Value("${token.jwt-secret}")
    private String jwtSecret;

    public String generateAccessToken(String email, String role, String departmentId) {
        final Map<String, Object> claim = Map.of(ROLE_CLAIM, role, DEPARTMENT_CLAIM, departmentId);
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(email)
                .addClaims(claim)
                .setNotBefore(Date.from(Instant.now()))
                .setExpiration(calculateDateExpired())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        final var email = claims.getSubject();
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                claims.get(ROLE_CLAIM).toString());
        final var departmentId = claims.get(DEPARTMENT_CLAIM);
        return new UsernamePasswordAuthenticationToken(email, departmentId, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    private Date calculateDateExpired() {
        return new Date(new Date().getTime() + JWT_VALIDITY_MILLISECONDS);
    }

}
