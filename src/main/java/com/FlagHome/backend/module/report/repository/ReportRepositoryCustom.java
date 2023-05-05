package com.FlagHome.backend.module.report.repository;

import com.FlagHome.backend.module.report.controller.dto.ReportResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepositoryCustom {
    boolean existsByMemberIdAndUrl(Long memberId, String url);
    List<ReportResponse> getAllReports();
}