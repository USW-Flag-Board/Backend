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

    public String sendCertification(String email, String result) {
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .subject(MailType.AUTH_EMAIL.getSubject())
                .content(MailType.AUTH_EMAIL.createMailForm(result))
                .build();

        emailService.sendEmail(mailRequest.toSendRequest());
        log.info("Email sent : " + email);
        return email;
    }

    public String sendFindIdResult(String email, String result) {
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .subject(MailType.FIND_ID.getSubject())
                .content(MailType.FIND_ID.createMailForm(result))
                .build();

        emailService.sendEmail(mailRequest.toSendRequest());
        log.info("Email sent : " + email);
        return email;
    }

    public String sendNewPassword(String email, String result) {
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .subject(MailType.REISSUE_PASSWORD.getSubject())
                .content(MailType.REISSUE_PASSWORD.createMailForm(result))
                .build();

        emailService.sendEmail(mailRequest.toSendRequest());
        log.info("Email sent : " + email);
        return email;
    }
}
