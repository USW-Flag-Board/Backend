package com.FlagHome.backend.domain.report.Service;

import com.FlagHome.backend.domain.report.dto.ReportResponse;
import com.FlagHome.backend.domain.report.entity.Report;
import com.FlagHome.backend.domain.report.repository.ReportRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    @Transactional
    public long create(Long memberId, Report report) {
        validateDuplicateReport(memberId, report.getReportedURL());
        Report createdReport = reportRepository.save(report);
        return createdReport.getId();
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReports() {
        return reportRepository.getAllReports();
    }

    @Transactional
    public void deleteReport(long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        reportRepository.delete(report);
    }

    @Transactional
    public void deleteAllReports() {
        reportRepository.deleteAllInBatch();
    }

    private void validateDuplicateReport(Long memberId,String url) {
        if (reportRepository.existsByMemberIdAndUrl(memberId, url)) {
            throw new CustomException(ErrorCode.ALREADY_REPORTED);
        }
    }
}
