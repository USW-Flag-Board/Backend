package com.Flaground.backend.module.admin;

import com.Flaground.backend.module.admin.service.AdminService;
import com.Flaground.backend.module.member.domain.BlackList;
import com.Flaground.backend.module.member.domain.BlackState.Ban;
import com.Flaground.backend.module.member.domain.BlackState.BlackState;
import com.Flaground.backend.module.member.domain.BlackState.Watching;
import com.Flaground.backend.module.member.domain.IssueRecord;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.enums.MemberStatus;
import com.Flaground.backend.module.member.domain.repository.BlackListRepository;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.report.domain.MemberReport;
import com.Flaground.backend.module.report.domain.Report;
import com.Flaground.backend.module.report.domain.ReportInfo;
import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import com.Flaground.backend.module.report.domain.repository.ReportRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AdminServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private BlackListRepository blackListRepository;

    @AfterEach
    void clean() {
        memberRepository.deleteAllInBatch();
        blackListRepository.deleteAllInBatch();
    }

    @Test
    void 신고_처리_테스트() {
        // given
        final ReportType reportType = ReportType.MEMBER;
        final ReportCategory category = ReportCategory.욕설;

        ReportInfo reportInfo = ReportInfo.builder()
                .reportType(reportType)
                .reportCategory(category)
                .build();

        IssueRecord issueRecord = new IssueRecord();
        Member member = memberRepository.save(Member.builder().issueRecord(issueRecord).build());
        Report report = reportRepository.save(MemberReport.builder().reported(member.getId()).reportInfo(reportInfo).build());

        // when
        String response = adminService.dealReport(report.getId());

        // then
        assertThat(response).isEqualTo(BlackState.DEFAULT_MESSAGE);

        Member findMember = memberRepository.findById(member.getId()).orElse(null);
        assertThat(findMember).isNotNull();
        assertThat(findMember.getState()).isInstanceOf(Watching.class);
        assertThat(findMember.getIssueRecord().getPenalty()).isEqualTo(category.getPenaltyPoint());
        assertThat(findMember.getIssueRecord().getReportedCount()).isEqualTo(1);

        Report findReport = reportRepository.findById(report.getId()).orElse(null);
        assertThat(findReport).isNull();
    }

    @Nested
    class 신고_처리_테스트2 {
        private final ReportType reportType = ReportType.MEMBER;
        private final ReportCategory category = ReportCategory.개인정보;
        private final String email = "gmlwh124@suwon.ac.kr";
        private Report report;

        private void initReport(Long memberId) {
            ReportInfo reportInfo = ReportInfo.builder()
                    .reportType(reportType)
                    .reportCategory(category)
                    .build();

            report = reportRepository.save(MemberReport.builder().reported(memberId).reportInfo(reportInfo).build());
        }

        @Test
        void 신고_정지_테스트() {
            // given
            IssueRecord issueRecord = new IssueRecord();
            Member member = memberRepository.save(Member.builder().email(email).issueRecord(issueRecord).build());
            initReport(member.getId());

            // when
            String response = adminService.dealReport(report.getId());

            // then
            assertThat(response).isEqualTo("계정이 3일 정지되었습니다.");

            Member findMember = memberRepository.findById(member.getId()).orElse(null);
            assertThat(findMember).isNotNull();
            assertThat(findMember.getStatus()).isEqualTo(MemberStatus.BANNED);
            assertThat(findMember.getState()).isInstanceOf(Ban.class);
            assertThat(findMember.getIssueRecord().getReportedCount()).isEqualTo(1);
            assertThat(findMember.getIssueRecord().getPenalty()).isEqualTo(category.getPenaltyPoint());

            Report findReport = reportRepository.findById(report.getId()).orElse(null);
            assertThat(findReport).isNull();

            Optional<BlackList> blackList = blackListRepository.findByEmail(email);
            assertThat(blackList.isPresent()).isTrue();
        }

        @Test
        void 신고_추방_테스트() {
            // given
            IssueRecord issueRecord = IssueRecord.builder().penalty(category.getPenaltyPoint()).build();
            Member member = memberRepository.save(Member.builder().email(email).issueRecord(issueRecord).build());
            initReport(member.getId());

            // when
            String response = adminService.dealReport(report.getId());

            // then
            assertThat(response).isEqualTo("계정을 추방시켰습니다.");

            Member findMember = memberRepository.findById(member.getId()).orElse(null);
            assertThat(findMember).isNotNull();
            assertThat(findMember.getStatus()).isEqualTo(MemberStatus.WITHDRAW);
            assertThat(findMember.getIssueRecord().getPenalty()).isEqualTo(category.getPenaltyPoint() * 2);

            Report findReport = reportRepository.findById(report.getId()).orElse(null);
            assertThat(findReport).isNull();

            boolean exists = blackListRepository.existsByEmail(email);
            assertThat(exists).isTrue();
        }
    }
}
