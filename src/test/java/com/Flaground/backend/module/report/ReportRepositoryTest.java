package com.Flaground.backend.module.report;

import com.Flaground.backend.common.RepositoryTest;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.domain.Reply;
import com.Flaground.backend.module.post.domain.repository.PostRepository;
import com.Flaground.backend.module.post.domain.repository.ReplyRepository;
import com.Flaground.backend.module.report.controller.dto.response.ReportResponse;
import com.Flaground.backend.module.report.domain.*;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import com.Flaground.backend.module.report.domain.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReportRepositoryTest extends RepositoryTest {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    private Member reporter;
    private Member reported;
    private Post post;
    private Reply reply;

    @BeforeEach
    void setup() {
        final String loginId = "gmlwh124";
        reporter = memberRepository.save(Member.builder().build());
        reported = memberRepository.save(Member.builder().loginId(loginId).build());
        post = postRepository.save(Post.builder().member(reported).build());
        reply = replyRepository.save(Reply.of(reported, post.getId(), "test"));
    }

    @Test
    @DisplayName("멤버 id로 모든 신고 이력이 삭제되어야 한다.")
    void deleteAllOfMemberTest() {
        // given
        ReportInfo memberReport = ReportInfo.builder().reportType(ReportType.MEMBER).build();
        ReportInfo postReport = ReportInfo.builder().reportType(ReportType.POST).build();
        ReportInfo replyReport = ReportInfo.builder().reportType(ReportType.REPLY).build();

        Report report = MemberReport.builder()
                .reporter(reporter.getId())
                .reported(reported.getId())
                .reportInfo(memberReport)
                .build();

        Report report2 = PostReport.builder()
                .reporter(reporter.getId())
                .reported(reported.getId())
                .reportInfo(postReport)
                .build();

        Report report3 = ReplyReport.builder()
                .reporter(reported.getId())
                .reported(reporter.getId())
                .reportInfo(replyReport)
                .build();

        reportRepository.saveAll(List.of(report, report2, report3));

        // when
        reportRepository.deleteAllOfMember(reported.getId());

        // then
        List<Report> reports = reportRepository.findAll();
        assertThat(reports).isEmpty();
    }

    @Test
    void 신고이력_가져오기_테스트() {
        // given
        Report memberReport = MemberReport.builder()
                .reporter(reporter.getId())
                .reported(reported.getId())
                .reportInfo(ReportInfo.builder().reportType(ReportType.MEMBER).build())
                .build();

        Report postReport = PostReport.builder()
                .reporter(reporter.getId())
                .reported(reported.getId())
                .reportInfo(ReportInfo.builder().reportType(ReportType.POST).build())
                .build();

        Report replyReport = ReplyReport.builder()
                .reporter(reporter.getId())
                .reported(reported.getId())
                .reportInfo(ReportInfo.builder().reportType(ReportType.REPLY).build())
                .build();

        reportRepository.saveAll(List.of(memberReport, postReport, replyReport));

        // when
        ReportResponse reports = reportRepository.getReports();

        // then
        assertThat(reports.getMemberReportResponses().size()).isEqualTo(1);
        assertThat(reports.getPostReportResponses().size()).isEqualTo(1);
        assertThat(reports.getReplyReportResponses().size()).isEqualTo(1);
    }
}
