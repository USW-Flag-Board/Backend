package com.Flaground.backend.module.report.domain.repository;

import com.Flaground.backend.module.report.controller.dto.response.ReportResponse;
import com.Flaground.backend.module.report.domain.enums.ReportType;

import java.util.List;

public interface ReportRepositoryCustom {
    boolean existByIdsAndType(Long reporter, Long reported, ReportType reportType);
    List<ReportResponse> getReports();
}
