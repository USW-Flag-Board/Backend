package com.Flaground.backend.module.report.controller.dto.request;

import com.Flaground.backend.global.annotation.EnumFormat;
import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReportRequest {
    @Schema(name = "신고 대상 로그인 ID")
    @NotBlank
    private String loginId;

    @Schema(name = "신고 카테고리", example = "음란물 / 홍보 / 도배 / 욕설 / 개인정보 / 기타")
    @EnumFormat(enumClass = ReportCategory.class)
    private ReportCategory reportCategory;

    @Schema(name = "신고 상세 내용")
    @NotNull
    private String detailExplanation;

    @Builder
    public MemberReportRequest(String loginId, ReportCategory reportCategory, String detailExplanation) {
        this.loginId = loginId;
        this.reportCategory = reportCategory;
        this.detailExplanation = detailExplanation;
    }
}
