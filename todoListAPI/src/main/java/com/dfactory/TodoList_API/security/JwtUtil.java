package com.dfactory.TodoList_API.security;

import com.dfactory.TodoList_API.model.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtil {
    private final Key key;
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        try {
            System.out.println("JWT Secret (raw): " + secret); // DEBUG
            byte[] decodeKey = Base64.getDecoder().decode(secret);
            this.key = Keys.hmacShaKeyFor(decodeKey);
        } catch(Exception e){
            e.printStackTrace(); // Mostrar error exacto
            throw new RuntimeException("Failed to initialize JWT key");
        }
    }

    public String generateToken(UserEntity user){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }

    public boolean isTokenValid(String token ,UserEntity user){
        String username = extractUsername(token);
        return username.equals(user.getEmail()) && !isTokenExpired(token);
    }
}
