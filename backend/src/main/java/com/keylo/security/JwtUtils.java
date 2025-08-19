package com.keylo.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.Base64;

@Component
public class JwtUtils {

    private final Key signingKey = loadSigningKeyFromEnv();

    private Key loadSigningKeyFromEnv() {
        Dotenv dotenv = Dotenv.load(); // Loads from .env file in project root
        String base64Key = dotenv.get("JWT_SECRET");

        if (base64Key == null || base64Key.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET is not set in .env");
        }

        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(base64Key);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("JWT_SECRET is not valid Base64", e);
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                       .setSigningKey(signingKey)
                       .build()
                       .parseClaimsJws(token)
                       .getBody();
        } catch (ExpiredJwtException e) {
            System.out.println("❌ JWT Token has expired: " + e.getMessage());
            throw e;
        } catch (JwtException e) {
            System.out.println("❌ JWT Token is invalid: " + e.getMessage());
            throw e;
        }
    }

    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            Date now = new Date();
            boolean expired = expiration.before(now);
            
            if (expired) {
                System.out.println("⏰ Token expired at: " + expiration + ", current time: " + now);
            }
            
            return expired;
        } catch (ExpiredJwtException e) {
            System.out.println("⏰ Token is expired: " + e.getMessage());
            return true;
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String email = extractEmail(token);
            boolean emailMatches = email.equals(userDetails.getUsername());
            boolean tokenNotExpired = !isTokenExpired(token);
            
            System.out.println(" Validating token for: " + email);
            System.out.println(" Email matches: " + emailMatches);
            System.out.println(" Token not expired: " + tokenNotExpired);
            
            return emailMatches && tokenNotExpired;
        } catch (ExpiredJwtException e) {
            System.out.println(" Cannot validate expired token: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println(" Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (1000 * 60 * 60 * 24)); // 24 hours instead of 10
        
        System.out.println(" Generating token for: " + email);
        System.out.println(" Token valid from: " + now);
        System.out.println(" Token expires at: " + expiryDate);
        
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String refreshToken(String email) {
        return generateToken(email);
    }
}