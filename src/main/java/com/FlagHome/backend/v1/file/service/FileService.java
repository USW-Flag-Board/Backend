package com.FlagHome.backend.v1.file.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3Client amazonS3Client;

    /**
     * <Parameter>
     *  dirName : "카테고리 이름" // 굳이 카테고리 이름을 넣어 분류할 필요가 없을것 같아 일단은 지웁니다 (2022.12.16 윤희승)
     *  imageName : "uuid.확장자"
     *
     * <Method>
     *  upload : MultipartFile을 S3에 저장
     *  delete : S3에서 제거
     */

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> upload(List<MultipartFile> fileList) {
        List<String> uploadedFileUriList = new ArrayList<>();

        for(MultipartFile eachFile : fileList) {
            String fileName = createFileName(eachFile);
            String uploadedFileUri = putS3(eachFile, fileName);
            uploadedFileUriList.add(uploadedFileUri);
        }

        return uploadedFileUriList;
    }

    public void delete(String imageName) {
        amazonS3Client.deleteObject(bucket, imageName);
    }

    /**
     * S3에 저장될 고유한 파일명을 리턴
     */
    private String createFileName(MultipartFile file) {
        String[] split = Objects.requireNonNull(file.getContentType().split("/"));
        String extension = split[split.length - 1];
        return UUID.randomUUID() + "." + extension;
    }

    /**
     * S3에 실제로 저장하는 메서드, URL을 리턴
     */
    private String putS3(MultipartFile file, String fileName) {
        amazonS3Client.putObject(createPutObjectRequest(file, fileName));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * S3에 저장할 때 사용하는 객체 리턴
     */
    private PutObjectRequest createPutObjectRequest(MultipartFile file, String fileName) {
        try {
            return new PutObjectRequest(bucket, fileName, file.getInputStream(), createObjectMetaData(file))
                    .withCannedAcl(CannedAccessControlList.PublicRead);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERT_FAIL);
        }
    }

    /**
     * PutObjectRequest를 만들기 위한 ObjectMetadata 리턴
     */
    private ObjectMetadata createObjectMetaData(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        return objectMetadata;
    }
}
