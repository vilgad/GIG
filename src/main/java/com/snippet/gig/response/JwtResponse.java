package com.snippet.gig.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private Long id;
    private String username;
    private String token;
    private String type = "Bearer";
    private String issueAt;
    private String expirationTime;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtResponse(Long id, String username, String token, String issueAt, String expirationTime,
                       Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.issueAt = issueAt;
        this.expirationTime = expirationTime;
        this.authorities = authorities;
    }
}