package com.FlagHome.backend.domain.report.Service;

import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.report.repository.ReportRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    public void validateDuplicateReport(String Url) {
        if (reportRepository.existsByUrl(Url)) {
            throw new CustomException(ErrorCode.REPORT_EXISTS);
        }
    }



}
