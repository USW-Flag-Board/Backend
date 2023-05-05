package com.FlagHome.backend.module.report.repository;

import com.FlagHome.backend.module.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {
    Optional<Report> findById(Long reportId);
}
