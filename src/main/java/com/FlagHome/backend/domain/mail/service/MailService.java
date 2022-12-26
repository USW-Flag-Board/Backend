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

    /**
     * 파라미터로 받은 이메일에 MailType에 해당하는 결과를 보낸다.
     * @param email
     * @param mailType (AUTH_EMAIL, FIND_ID, REISSUE_PASSWORD)
     * @param result (6자리 인증번호, 로그인 아이디, 재발급한 비밀번호)
     */
    public void sendMailByTypeAndResult(String email, MailType mailType, String result) {
        MailRequest mailRequest = MailRequest.builder()
                .to(email)
                .subject(mailType.getSubject())
                .content(mailType.createMailForm(result))
                .build();

        emailService.sendEmail(mailRequest.toSendRequest());
        log.info("Email sent : " + email);
    }
}
