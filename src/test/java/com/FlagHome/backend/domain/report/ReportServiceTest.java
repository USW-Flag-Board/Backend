package com.FlagHome.backend.domain.report;


import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.report.Service.ReportService;
import com.FlagHome.backend.domain.report.entity.Report;
import com.FlagHome.backend.domain.report.repository.ReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}
