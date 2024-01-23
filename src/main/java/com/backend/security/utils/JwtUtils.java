package com.backend.security.utils;

import com.backend.models.entities.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    public static final int EXPIRATION_TIME_IN_MINUTES = 1440;
    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public String generateJwt(Admin admin) {
        return generateJwt(new HashMap<>(), admin);
    }

    public  String generateJwt(Map<String, Object> extraClaims, Admin admin) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + EXPIRATION_TIME_IN_MINUTES * 1000 * 60);
        return Jwts.builder()
                .claims(extraClaims)
                .subject(admin.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String jwtToken) {
        return getClaim(jwtToken, Claims::getSubject);
    }

    private <T> T getClaim(String jwtToken, Function<Claims, T> claimsTFunction) {
        final Claims claims = getAllClaims(jwtToken);
        return claimsTFunction.apply(claims);
    }

    private Claims getAllClaims(String jwtToken) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
}
