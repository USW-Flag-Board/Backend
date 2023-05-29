package com.Flaground.backend.infra.aws.s3.controller;

import com.Flaground.backend.global.annotation.EnumFormat;
import com.Flaground.backend.global.common.response.ApplicationResponse;
import com.Flaground.backend.global.utility.SecurityUtils;
import com.Flaground.backend.infra.aws.s3.domain.ImageDirectory;
import com.Flaground.backend.infra.aws.s3.service.AwsS3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "image", description = "게시글 이미지 API")
@Validated
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private final AwsS3Service awsS3Service;

    @Tag(name = "image")
    @Operation(summary = "이미지 업로드 API", description = "[토큰필요] 이미지 접근용 Key를 돌려받는다.")
    @ResponseStatus(CREATED)
    @PostMapping("/{directory}")
    public ApplicationResponse<String> uploadImage(@PathVariable @EnumFormat(enumClass = ImageDirectory.class) ImageDirectory directory,
                                                   @RequestPart MultipartFile image) {
        SecurityUtils.getMemberId();
        String key = awsS3Service.upload(image, directory.getDirectory());
        return new ApplicationResponse<>(key);
    }
}
