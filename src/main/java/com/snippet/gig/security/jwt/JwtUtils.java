package com.snippet.gig.security.jwt;

import com.snippet.gig.response.JwtResponse;
import com.snippet.gig.utils.UserDetail;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    public JwtResponse generateTokenForUser(Authentication authentication) {
        UserDetail userPrincipal = (UserDetail) authentication.getPrincipal();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusNanos(expirationTime * 1_000_000L);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        String formattedExpiry = expiryDate.format(formatter);

        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        String token = Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getUser().getId())
                .claim("roles", roles)
                .issuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key(), SignatureAlgorithm.HS256).compact();

        return new JwtResponse(
                userPrincipal.getUser().getId(),
                userPrincipal.getUsername(),
                token,
                formattedNow,    // "2025-06-12 18:31:40"
                formattedExpiry, // "2025-06-12 19:31:40"
                userPrincipal.getAuthorities()
        );
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }
}
