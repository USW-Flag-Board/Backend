package com.FlagHome.backend.domain.mail.service;

import com.FlagHome.backend.domain.mail.MailType;
import com.FlagHome.backend.domain.mail.dto.MailRequest;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MailService {
    private final AmazonSimpleEmailService emailService;

    public void sendCertification(String email, String result) {
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .subject(MailType.AUTH_EMAIL.getSubject())
                .content(MailType.AUTH_EMAIL.createMailForm(result))
                .build();

        emailService.sendEmail(mailRequest.toSendRequest());
        log.info("Email sent : " + email);
    }

    public void sendFindCertification(String email, String result) {
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .subject(MailType.FIND_AUTH.getSubject())
                .content(MailType.FIND_AUTH.createMailForm(result))
                .build();

        emailService.sendEmail(mailRequest.toSendRequest());
        log.info("Email sent : " + email);
    }
}
