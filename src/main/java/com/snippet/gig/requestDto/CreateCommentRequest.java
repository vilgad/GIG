package com.snippet.gig.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    @NotNull @NotBlank @NotEmpty
    private String content;
    @NotNull @NotBlank @NotEmpty
    private Long taskId;
    @NotNull @NotBlank @NotEmpty
    private String username;

    private Long parentCommentId;
}


