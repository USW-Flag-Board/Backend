package com.FlagHome.backend.v1.member.mail;

import com.amazonaws.services.simpleemail.model.*;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MailRequest {
    private static final String FROM = "no-reply@FLAG.com";

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
        final Destination destination = new Destination()
                .withToAddresses(this.to);

        final Message message = new Message()
                .withSubject(createContent(this.subject))
                .withBody(new Body()
                        .withHtml(createContent(this.content)));

        return new SendEmailRequest()
                .withSource(FROM)
                .withDestination(destination)
                .withMessage(message);
    }

    private Content createContent(String text) {
        return new Content()
                .withCharset("UTF-8")
                .withData(text);
    }
}
