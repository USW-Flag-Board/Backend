package com.Flaground.backend.infra.aws.ses.service;

import com.Flaground.backend.infra.aws.ses.MailType;
import com.Flaground.backend.infra.aws.ses.dto.MailRequest;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsSESServiceImpl implements AwsSESService {
    private final AmazonSimpleEmailService emailService;

    @Override
    public void sendCertification(String email, String result) {
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .subject(MailType.AUTH_EMAIL.getSubject())
                .content(MailType.AUTH_EMAIL.createMailForm(result))
                .build();

        emailService.sendEmail(mailRequest.toSendRequest());
        log.info("Email sent : " + email);
    }

    @Override
    public void sendFindCertification(String email, String result) {
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .subject(MailType.FIND_AUTH.getSubject())
                .content(MailType.FIND_AUTH.createMailForm(result))
                .build();

        emailService.sendEmail(mailRequest.toSendRequest());
        log.info("Email sent : " + email);
    }

    @Override
    public void sendChangeSleep(String email) {
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .subject(MailType.SLEEP_EMAIL.getSubject())
                .content(MailType.SLEEP_EMAIL.createMailForm())
                .build();

        emailService.sendEmail(mailRequest.toSendRequest());
        log.info("Email sent : " + email);
    }
}
