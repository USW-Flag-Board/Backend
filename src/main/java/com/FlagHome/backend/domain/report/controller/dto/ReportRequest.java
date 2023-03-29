package com.FlagHome.backend.domain.report.controller.dto;


import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.report.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportRequest {
    @Schema(name = "신고자", required = true, example = "문희조")
    private Member reporter;

    @Schema(name = "피신고자", required = true, example = "고건")
    private Member reported;

    @Schema(name = "신고사유", required = true, example = "욕설")
    private ReportType reportType;

    @Schema(name = "세부내용", required = true, example = "테트리스 졌다고 욕함")
    private String detailReason;

    @Schema(name = "신고경로", required = true)
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
