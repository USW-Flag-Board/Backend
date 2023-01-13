package com.FlagHome.backend.domain.member.dto;

import com.FlagHome.backend.domain.member.Major;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProfileRequest {
    @Schema(description = "자기소개", required = true, example = "백엔드 개발자 문희조입니다.")
    private String bio;

    @Schema(description = "핸드폰 번호", required = true, example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "전공", required = true, example = "컴퓨터SW")
    private Major major;

    @Schema(description = "학번", required = true, example = "19017041")
    private String studentId;
}
