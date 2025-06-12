package com.snippet.gig.requestDto;

import lombok.*;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateProjectRequest {
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
