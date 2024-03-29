package com.Flaground.backend.infra.aws.ses.dto;

import com.amazonaws.services.simpleemail.model.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailRequest {
    private static final String FROM = "no-reply@flaground.kr";

    private String to;
    private String subject;
    private String content;

    @Builder
    public MailRequest(String to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public SendEmailRequest toSendRequest() {
        final Destination destination = createDestination();
        final Message message = createMessage();

        return new SendEmailRequest()
                .withSource(FROM)
                .withDestination(destination)
                .withMessage(message);
    }

    private Destination createDestination() {
        return new Destination().withToAddresses(this.to);
    }

    private Message createMessage() {
        return new Message()
                .withSubject(createContent(this.subject))
                .withBody(new Body().withHtml(createContent(this.content)));
    }

    private Content createContent(String text) {
        return new Content()
                .withCharset("UTF-8")
                .withData(text);
    }
}
