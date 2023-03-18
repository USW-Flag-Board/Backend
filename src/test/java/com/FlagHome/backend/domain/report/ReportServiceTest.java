package com.FlagHome.backend.domain.report;

import com.FlagHome.backend.domain.member.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.report.Service.ReportService;
import com.FlagHome.backend.domain.report.controller.dto.ReportResponse;
import com.FlagHome.backend.domain.report.repository.ReportRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
public class ReportServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;


    @Test
    @DisplayName("신고하기 성공")
    void reportSuccessTest() {
        //given
        Member reporter = memberRepository.saveAndFlush(Member.builder().build());
        Member reported = memberRepository.saveAndFlush(Member.builder().build());

        String reportedUrl = "?????/freeboard/2";
        ReportType reportType = ReportType.욕설;
        String detailReason = "그냥 마음에 안듦";

        Report report = Report.builder()
                .reportedURL(reportedUrl)
                .reporter(reporter)
                .reported(reported)
                .reportType(reportType)
                .detailReason(detailReason)
                .build();

        //when
        long id = reportService.create(reporter.getId(), report);

        //then
        Report findReport = reportRepository.findById(report.getId()).get();
        assertThat(findReport.getId()).isNotNull();
        assertThat(findReport.getReporter().getId()).isEqualTo(reporter.getId());
        assertThat(findReport.getDetailReason()).isEqualTo(detailReason);
    }

    @Test
    @DisplayName("신고 삭제하기 테스트")
    void deleteReportTest() {
        //given
        String reportedURL = "www.haha";
        ReportType reportType = ReportType.기타;

        Report report = reportRepository.saveAndFlush(Report.builder()
                .reportedURL(reportedURL)
                .reportType(reportType)
                .build());

        //when
        reportService.deleteReport(report.getId());

        //then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> reportRepository.findById(report.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND)))
                .withMessage(ErrorCode.REPORT_NOT_FOUND.getMessage());

    }

    @Test
    @DisplayName("모든신고 삭제하기 테스트")
    void deleteAllReportsTest() {
        //given
        String reportedURL1 = "www.haha";
        String reportedURL2 = "www.haha2";

        reportRepository.save(Report.builder().reportedURL(reportedURL1).build());
        reportRepository.save(Report.builder().reportedURL(reportedURL2).build());

        //when
        reportService.deleteAllReports();

        //then
        List<ReportResponse> responses = reportService.getAllReports();
        assertThat(responses.size()).isEqualTo(0);
    }
}
