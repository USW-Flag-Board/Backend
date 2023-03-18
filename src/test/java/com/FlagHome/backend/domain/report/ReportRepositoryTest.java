package com.FlagHome.backend.domain.report;

import com.FlagHome.backend.domain.member.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.report.controller.dto.ReportResponse;
import com.FlagHome.backend.domain.report.repository.ReportRepository;
import com.FlagHome.backend.global.config.QueryDslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(QueryDslConfig.class)
@DataJpaTest
public class ReportRepositoryTest {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void testSetUp() {
        member = memberRepository.save(Member.builder().build());
    }

    @Test
    @DisplayName("신고 확인하기 테스트")
    void validateDuplicateReportTest() {
        // given
        String reportedUrl = "thisiscorrect";
        String wrongUrl = "thisiswrong";

        reportRepository.save(Report.builder()
                .reporter(member)
                .reportedURL(reportedUrl)
                .build());

        // when
        boolean shouldBeTrue = reportRepository.existsByMemberIdAndUrl(member.getId(), reportedUrl);
        boolean shouldBeFalse = reportRepository.existsByMemberIdAndUrl(member.getId(), wrongUrl);

        // then
        assertThat(shouldBeTrue).isTrue();
        assertThat(shouldBeFalse).isFalse();
    }

    @Test
    @DisplayName("모든 신고 가져오기 테스트")
    void getReportTest() {
        //given
        Report report1 = Report.builder()
                .reported(member)
                .reportType(ReportType.기타)
                .build();

        Report report2 = Report.builder()
                .reported(member)
                .reportType(ReportType.도배)
                .build();

        reportRepository.saveAll(Arrays.asList(report1, report2));

        //when
        List<ReportResponse> responses = reportRepository.getAllReports();

        //then
        assertThat(responses.size()).isEqualTo(2);
    }
}
