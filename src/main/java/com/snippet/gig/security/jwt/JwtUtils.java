package com.snippet.gig.security.jwt;

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
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {
    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    public String generateTokenForUser(Authentication authentication) {
        UserDetail userPrincipal = (UserDetail) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getUser().getId())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256).compact();
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
