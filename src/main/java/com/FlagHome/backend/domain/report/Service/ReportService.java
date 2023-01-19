package com.FlagHome.backend.domain.report.Service;

import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    /*
    @Transactional
    public long create(Long memberId, Report report) {
        validateDuplicateReport(memberId, report.getReportedURL());

        return reportRepository.save(report).getId();
    }*/


/*
    private void validateDuplicateReport(Long memberId,String url) {
        if (reportRepository.existsByMemberIdAndUrl(memberId, url)) {
            throw new CustomException(ErrorCode.ALREADY_REPORTED);
        }
    }*/
}
