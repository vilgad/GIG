package com.snippet.gig.requestDto;

import com.snippet.gig.enums.Priority;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateTaskRequest {
    @NotNull(message = "title cannot be null")
    @NotEmpty(message = "title cannot be empty")
    private String title;

    private String description;

    @NotNull(message = "dueDate cannot be null")
    @NotEmpty(message = "dueDate cannot be empty")
    private LocalDateTime dueDate;

    private Priority priority;
}
