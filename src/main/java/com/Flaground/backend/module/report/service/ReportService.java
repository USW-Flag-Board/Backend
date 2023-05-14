package com.Flaground.backend.module.report.service;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.service.MemberService;
import com.Flaground.backend.module.report.controller.dto.response.ReportResponse;
import com.Flaground.backend.module.report.domain.Report;
import com.Flaground.backend.module.report.domain.ReportData;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import com.Flaground.backend.module.report.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberService memberService;

    public List<ReportResponse> getReports() {
        return reportRepository.getReports();
    }

    @Transactional
    public void reportMember(Long memberId, ReportData<String> reportData) {
        Member reported = memberService.findByLoginId(reportData.getTarget());
        validateDuplicate(memberId, reported.getId(), reportData.getReportType());
        reportRepository.save(Report.member(memberId, reported.getId(), reportData));
    }

    @Transactional
    public void reportContent(Long memberId, ReportData<Long> reportData) {
        validateDuplicate(memberId, reportData.getTarget(), reportData.getReportType());
        reportRepository.save(Report.content(memberId, reportData));
    }

    private void validateDuplicate(Long reporterId, Long reportedId, ReportType reportType) {
        if (isReported(reporterId, reportedId, reportType)) {
            throw new CustomException(ErrorCode.ALREADY_REPORTED);
        }
    }

    private boolean isReported(Long reporterId, Long reportedId, ReportType reportType) {
        return reportRepository.existByIdsAndType(reporterId, reportedId, reportType);
    }
}
