package com.FlagHome.backend.v1.member.mail;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MailService {
    private final AmazonSimpleEmailService emailService;

    public void sendMail(String email, MailType mailType, String result) {
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .subject(mailType.getSubject())
                .content(mailType.createMailForm(result))
                .build();

        emailService.sendEmail(mailRequest.toSendRequest());
        log.info("Email sent : " + email);
    }
}
