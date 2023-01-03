package com.FlagHome.backend.global.utility;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3Client amazonS3Client;

    /**
     * <Parameter>
     *  dirName : "카테고리 이름"
     *  imageName : "uuid.확장자"
     *
     * <Method>
     *  upload : MultipartFile을 S3에 저장
     *  delete : S3에서 제거
     */

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String dirName) {
        String fileName = createFileName(file, dirName);
        String uploadFileUrl = putS3(file, fileName);
        return uploadFileUrl;
    }

    public void delete(String imageName, String dirName) {
        String fileName = dirName + "/" + imageName;
        amazonS3Client.deleteObject(bucket, fileName);
    }

    /**
     * S3에 저장될 고유한 파일명을 리턴
     */
    private String createFileName(MultipartFile file, String dirName) {
        String[] split = Objects.requireNonNull(file.getContentType().split("/"));
        String extension = split[split.length - 1];
        return dirName + "/" + UUID.randomUUID() + "." + extension;
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
