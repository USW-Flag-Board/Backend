package com.Flaground.backend.module.report.domain.repository;

import com.Flaground.backend.module.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {
}
