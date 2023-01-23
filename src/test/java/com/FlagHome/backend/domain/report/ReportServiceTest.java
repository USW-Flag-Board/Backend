package com.FlagHome.backend.domain.report;

import ch.qos.logback.classic.spi.TurboFilterList;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.report.Service.ReportService;
import com.FlagHome.backend.domain.report.dto.ReportResponse;
import com.FlagHome.backend.domain.report.entity.Report;
import com.FlagHome.backend.domain.report.repository.ReportRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ReportServiceTest {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportService reportService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("모든 신고 가져오기 테스트")
    void getReportTest() {
        //given

        Member member = memberRepository.save(Member.builder().build());

        Report report1 = Report.builder()
                .reported(member)
                .reportType(ReportType.기타)
                .build();

        Report report2 = Report.builder()
                .reported(member)
                .reportType(ReportType.도배)
                .build();

        reportRepository.saveAll(Arrays.asList(report1,report2));

        //when
        List<ReportResponse> reportResponseLIST = reportRepository.getAllReports();

        //then
        assertThat(reportResponseLIST.size()).isEqualTo(2);
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
        String reportedURL = "www.haha";

        Report report1 = reportRepository.save(Report.builder().build());
        Report report2 = reportRepository.save(Report.builder().reportedURL(reportedURL).build());
        Report report3 = reportRepository.saveAndFlush(Report.builder().reportedURL(reportedURL).build());

        //when
        reportService.deleteAllReports();

        //then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> reportRepository.findById(report3.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND)))
                .withMessage(ErrorCode.REPORT_NOT_FOUND.getMessage());
    }

}
