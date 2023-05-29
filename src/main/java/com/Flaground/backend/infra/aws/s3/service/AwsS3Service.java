package com.Flaground.backend.infra.aws.s3.service;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
    String upload(MultipartFile file, String directory);

    void delete(String key);
}
