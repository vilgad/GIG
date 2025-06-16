package com.snippet.gig.requestDto;

import com.snippet.gig.enums.Priority;
import com.snippet.gig.enums.Status;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTaskRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private Status status;
}
