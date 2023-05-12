package com.Flaground.backend.module.report.domain.repository;

import com.Flaground.backend.module.report.domain.enums.ReportType;

public interface ReportRepositoryCustom {
    boolean existByIdsAndType(Long reporter, Long reported, ReportType reportType);
}
