package com.snippet.gig.requestDto;

import jakarta.validation.constraints.NotBlank;


public class LoginRequest {
    @NotBlank(message = "Invalid credentials")
    private String email;

    @NotBlank(message = "Invalid credentials")
    private String password;

    public @NotBlank(message = "Invalid credentials") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Invalid credentials") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Invalid credentials") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Invalid credentials") String password) {
        this.password = password;
    }
}
