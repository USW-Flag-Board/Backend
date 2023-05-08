package com.Flaground.backend.module.report.controller.dto;


import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.report.ReportType;
import com.Flaground.backend.global.annotation.EnumFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportRequest {
    @Schema(name = "신고자", example = "문희조")
    private Member reporter;

    @Schema(name = "피신고자", example = "고건")
    private Member reported;

    @Schema(name = "신고사유", example = "음란물 / 홍보 / 도배 / 욕설 / 개인정보 / 기타")
    @EnumFormat(enumClass = ReportType.class)
    private ReportType reportType;

    @Schema(name = "세부내용", example = "테트리스 졌다고 욕함")
    private String detailReason;

    @Schema(name = "신고경로")
    private String reportedUrl;

    @Builder
    public ReportRequest(Member reporter, Member reported, ReportType reportType, String detailReason, String reportedUrl) {
        this.reporter = reporter;
        this.reported = reported;
        this.reportType = reportType;
        this.detailReason = detailReason;
        this.reportedUrl = reportedUrl;
    }
}
