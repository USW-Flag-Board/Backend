package com.Flaground.backend.module.report.controller.mapper;

import com.Flaground.backend.module.report.controller.dto.request.ContentReportRequest;
import com.Flaground.backend.module.report.controller.dto.request.MemberReportRequest;
import com.Flaground.backend.module.report.domain.AbstractReport;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ReportMapper {
    AbstractReport<Long> mapFrom(ContentReportRequest request);

    @Mapping(target = "reportType", expression = "java(getDefault())")
    @Mapping(source = "loginId", target = "target")
    AbstractReport<String> mapFrom(MemberReportRequest request);

    default ReportType getDefault() {
        return ReportType.MEMBER;
    }
}
