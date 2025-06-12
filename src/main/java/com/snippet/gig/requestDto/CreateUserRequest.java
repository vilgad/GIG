package com.snippet.gig.requestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    private String name;

    // TODO(Fix for dob)
    @DateTimeFormat
    private LocalDate dob;

    @NotEmpty(message = "username cannot be empty")
    @NotNull(message = "username cannot be null")
    @Size(min = 6, message = "username must be of at-least 6 characters")
    private String username;

    @Email(message = "provide a valid email address")
    @NotNull(message = "email cannot be null")
    private String email;

    @NotNull(message = "password cannot be null")
    @NotEmpty(message = "password cannot be empty")
    @Size(min = 8, message = "password must be of at-least 8 characters")
    private String password;
}
