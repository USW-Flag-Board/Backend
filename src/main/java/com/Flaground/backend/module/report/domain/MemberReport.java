package com.Flaground.backend.module.report.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReport extends Report {
    @Column
    private String loginId;

    @Builder
    public MemberReport(Long reporter, Long reported, ReportInfo reportInfo, String loginId) {
        super(reporter, reported, reportInfo);
        this.loginId = loginId;
    }

    public static MemberReport of(Long reporter, Long reported, AbstractReport<String> abstractReport) {
        return MemberReport.builder()
                .reporter(reporter)
                .reported(reported)
                .loginId(abstractReport.getTarget())
                .reportInfo(ReportInfo.from(abstractReport))
                .build();
    }
}
