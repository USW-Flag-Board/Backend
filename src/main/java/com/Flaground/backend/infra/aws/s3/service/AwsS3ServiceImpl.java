package com.Flaground.backend.infra.aws.s3.service;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {
    private static final String SLASH = "/";
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile file, String directory) {
        String key = putS3(file, directory);
        log.info("[Image Uploaded] directory : {}, key : {}", directory , key);
        return key;
    }

    @Override
    public void delete(String key) {
        String[] split = key.split(SLASH);
        amazonS3Client.deleteObject(bucket, key);
        log.info("[File Deleted] Directory : {}, file : {}", split[0], split[1]);
    }

    @Override
    public void deleteAll(List<String> keys) {
        DeleteObjectsRequest deleteObjectsRequest = deleteObjectsRequest(keys);
        amazonS3Client.deleteObjects(deleteObjectsRequest);
        log.info("[Images Deleted] {}", keys);
    }

    @Override // test용
    public int size() {
        return 0;
    }

    private String putS3(MultipartFile file, String directory) {
        String key = createKey(file, directory);
        amazonS3Client.putObject(putObjectRequest(file, key));
        return key;
    }

    private String createKey(MultipartFile file, String directory) {
        String[] split = Objects.requireNonNull(Objects.requireNonNull(file.getContentType()).split(SLASH));
        String extension = split[split.length - 1];
        return directory + SLASH + UUID.randomUUID() + "." + extension;
    }

    private PutObjectRequest putObjectRequest(MultipartFile file, String key) {
        try {
            return new PutObjectRequest(bucket, key, file.getInputStream(), objectMetaData(file))
                        .withCannedAcl(CannedAccessControlList.PublicRead);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERT_FAIL);
        }
    }

    private DeleteObjectsRequest deleteObjectsRequest(List<String> keys) {
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket);
        deleteObjectsRequest.setKeys(toKeyVersions(keys));
        return deleteObjectsRequest;
    }

    private List<KeyVersion> toKeyVersions(List<String> keys) {
        return keys.stream()
                .map(KeyVersion::new)
                .toList();
    }

    private ObjectMetadata objectMetaData(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }
}
