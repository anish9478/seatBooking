package com.anish.seatbooking.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value(("${jwt.expiration : 86400000}"))
    private Long expiration;
    private SecretKey getSignInkey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String generateToken(String username) {
        return Jwts.builder().subject(username).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInkey()).compact();
    }
    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(getSignInkey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
