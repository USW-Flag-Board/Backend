package com.FlagHome.backend.domain.report.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepositoryCustom {
    boolean existsByMemberIdAndUrl(Long memberId, String url);
}
