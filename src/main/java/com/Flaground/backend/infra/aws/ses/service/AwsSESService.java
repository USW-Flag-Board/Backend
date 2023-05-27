package com.Flaground.backend.infra.aws.ses.service;

public interface AwsSESService {
    void sendCertification(String email, String result);
    void sendFindCertification(String email, String result);
    void sendChangeSleep(String email);
}
