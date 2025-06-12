package com.snippet.gig.requestDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateProjectRequest {
    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotNull(message = "description cannot be null")
    @NotEmpty(message = "description cannot be empty")
    private String description;

    @NotNull(message = "start date cannot be null")
    @NotEmpty(message = "start date cannot be empty")
    private LocalDate startDate;

    @NotNull(message = "end date cannot be null")
    @NotEmpty(message = "end date cannot be empty")
    private LocalDate endDate;
}
