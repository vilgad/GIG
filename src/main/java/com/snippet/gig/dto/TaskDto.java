package com.snippet.gig.dto;

import com.snippet.gig.enums.Priority;
import com.snippet.gig.enums.Status;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private Priority priority;
    private Status status;
}
