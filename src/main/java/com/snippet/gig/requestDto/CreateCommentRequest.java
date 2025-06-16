package com.snippet.gig.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> mentionedUsers = new ArrayList<>();
}


