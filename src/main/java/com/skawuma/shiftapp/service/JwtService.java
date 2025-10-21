package com.skawuma.shiftapp.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * @author samuelkawuma
 * @package com.skawuma.shiftapp.service
 * @project Shift-App
 * @date 10/12/25
 */
@Service
public class JwtService {

    private final Key key;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration:86400000}") long expirationMs // default 24h
    ) {
        // Ensure the secret has sufficient length for HMAC-SHA256 (≥256 bits)
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    /**
     * Generate a signed JWT for a given username and optional claims.
     */
    public String generateToken(String username, Map<String, Object> claims) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract username (subject) from JWT.
     */
    public String extractUsername(String token) {
        return parseToken(token).getBody().getSubject();
    }

    /**
     * Parse and validate the token signature + expiration.
     */
    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    /**
     * Validates token subject and expiration.
     */
    public boolean isTokenValid(String token, Object user) {
        try {
            Jws<Claims> parsed = parseToken(token);
            String username = parsed.getBody().getSubject();
            Date exp = parsed.getBody().getExpiration();
            boolean notExpired = exp.after(new Date());
            return username != null && notExpired;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}