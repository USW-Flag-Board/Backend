package com.FlagHome.backend.domain.report.repository;

import com.FlagHome.backend.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
/*
    boolean existsByMemberIdAndUrl(Long memberId, String url);*/
}
