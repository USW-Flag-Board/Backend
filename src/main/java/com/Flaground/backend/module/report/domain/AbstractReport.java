package com.Flaground.backend.module.report.domain;

import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AbstractReport<T> {
    private T target;
    private ReportType reportType;
    private ReportCategory reportCategory;
    private String detailExplanation;
}
