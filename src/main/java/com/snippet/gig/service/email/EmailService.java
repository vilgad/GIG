package com.snippet.gig.service.email;

import com.snippet.gig.exception.EmailDeliveringException;
import com.snippet.gig.requestDto.SendEmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService implements IEmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    @Override
    public void sendEmail(SendEmailRequest request) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(request.getTo());
            mail.setSubject(request.getSubject());
            mail.setText(request.getBody());
            javaMailSender.send(mail);
        } catch (Exception e) {
            throw new EmailDeliveringException("Facing some problems while sending email, Try again later");
        }
    }
}
