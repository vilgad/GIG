package com.snippet.gig.controller;

import com.snippet.gig.requestDto.SendEmailRequest;
import com.snippet.gig.response.ApiResponse;
import com.snippet.gig.service.email.IEmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/email")
@Tag(name = "Email APIs")
public class EmailController {
    private final IEmailService emailService;

    public EmailController(IEmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/public/send-email")
    public ResponseEntity<ApiResponse> createComment(
            @RequestBody SendEmailRequest request
    ) {
        emailService.sendEmail(request);
        return ResponseEntity.ok(
                new ApiResponse(
                        "email sent Successfully",
                        null
                ));
    }
}
