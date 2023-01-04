package com.FlagHome.backend.domain.member.dto;

import com.FlagHome.backend.domain.member.Major;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateProfileRequest {
    @Parameter(description = "자기소개", required = true, example = "백엔드 개발자 문희조입니다.")
    private String bio;

    @Parameter(description = "핸드폰 번호", required = true, example = "010-1234-5678")
    private String phoneNumber;

    @Parameter(description = "전공", required = true, example = "컴퓨터SW")
    private Major major;

    @Parameter(description = "학번", required = true, example = "19017041")
    private String studentId;
}
