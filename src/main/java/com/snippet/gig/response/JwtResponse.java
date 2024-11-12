package com.snippet.gig.response;

public class JwtResponse {
    private Long id;
    private String token;

    public JwtResponse() {
    }

    public JwtResponse(String token, Long id) {
        this.token = token;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
