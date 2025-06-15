package com.snippet.gig.service.email;

import com.snippet.gig.requestDto.SendEmailRequest;

public interface IEmailService {
    void sendEmail(SendEmailRequest request);
}
