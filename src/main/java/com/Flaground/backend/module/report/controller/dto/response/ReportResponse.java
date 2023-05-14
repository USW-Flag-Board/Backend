package com.Flaground.backend.module.report.controller.dto.response;

import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportResponse {
    private Long reported;
    private String type;
    private String category;
    private String detailReason;
    private LocalDateTime createdAt;

    @QueryProjection
    public ReportResponse(Long reported, ReportType type, ReportCategory category, String detailReason, LocalDateTime createdAt) {
        this.reported = reported;
        this.type = type.getType();
        this.category = category.toString();
        this.detailReason = detailReason;
        this.createdAt = createdAt;
    }
}
