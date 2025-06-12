package com.snippet.gig.requestDto;

import lombok.*;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTaskRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String priority;
    private String status;
}
