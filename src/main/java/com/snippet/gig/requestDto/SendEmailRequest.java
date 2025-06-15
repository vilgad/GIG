package com.snippet.gig.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {
    @NotEmpty @NotNull @NotBlank
    private String to;
    @NotEmpty @NotNull @NotBlank
    private String subject;
    @NotEmpty @NotNull @NotBlank
    private String body;
}
