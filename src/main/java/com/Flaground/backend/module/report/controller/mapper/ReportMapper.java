package com.Flaground.backend.module.report.controller.mapper;

import com.Flaground.backend.module.report.controller.dto.request.ContentReportRequest;
import com.Flaground.backend.module.report.controller.dto.request.MemberReportRequest;
import com.Flaground.backend.module.report.domain.ReportData;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ReportMapper {
    ReportData<Long> mapFrom(ContentReportRequest request);

    @Mapping(target = "reportType", expression = "java(getDefault())")
    ReportData<String> mapFrom(MemberReportRequest request);

    default ReportType getDefault() {
        return ReportType.MEMBER;
    }
}
