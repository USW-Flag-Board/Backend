package com.FlagHome.backend.global.infra.aws.s3;

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
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String directory) {
        String fileName = createFileName(file, directory);
        String fileUrl = putS3(file, fileName);
        log.info("[File Uploaded] directory : {}, URL : {}", directory , fileUrl);
        return fileUrl;
    }

//    public List<String> upload(List<MultipartFile> fileList) {
//        List<String> uploadedFileUriList = new ArrayList<>();
//
//        for(MultipartFile eachFile : fileList) {
//            String fileName = createFileName(eachFile);
//            String uploadedFileUri = putS3(eachFile, fileName);
//            uploadedFileUriList.add(uploadedFileUri);
//        }
//
//        return uploadedFileUriList;
//    }

    public void delete(String imageName, String directory) {
        String fileName = directory + "/" + imageName;
        amazonS3Client.deleteObject(bucket, fileName);
        log.info("[File Deleted] Directory : {}, FileName : {}", directory, fileName);
    }

    private String createFileName(MultipartFile file, String directory) {
        String[] split = Objects.requireNonNull(file.getContentType().split("/"));
        String extension = split[split.length - 1];
        return directory + "/" + UUID.randomUUID() + "." + extension;
    }

    private String putS3(MultipartFile file, String fileName) {
        amazonS3Client.putObject(createPutObjectRequest(file, fileName));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private PutObjectRequest createPutObjectRequest(MultipartFile file, String fileName) {
        try {
            return new PutObjectRequest(bucket, fileName, file.getInputStream(), createObjectMetaData(file))
                    .withCannedAcl(CannedAccessControlList.PublicRead);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_CONVERT_FAIL);
        }
    }

    private ObjectMetadata createObjectMetaData(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }
}
