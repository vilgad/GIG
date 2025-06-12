package com.snippet.gig.requestDto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String name;
    private LocalDate dob;
}
