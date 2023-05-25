package com.Flaground.backend.module.report.domain.repository.implementation;

import com.Flaground.backend.module.report.controller.dto.response.*;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import com.Flaground.backend.module.report.domain.repository.ReportRepositoryCustom;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Flaground.backend.module.report.domain.QMemberReport.memberReport;
import static com.Flaground.backend.module.report.domain.QPostReport.postReport;
import static com.Flaground.backend.module.report.domain.QReplyReport.replyReport;
import static com.Flaground.backend.module.report.domain.QReport.report;

@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {
    private final JPQLQueryFactory queryFactory;

    @Override
    public boolean existByIdsAndType(Long reporter, Long reported, ReportType reportType) {
        return queryFactory
                .selectFrom(report)
                .where(report.reporter.eq(reporter),
                        report.reported.eq(reported),
                        report.reportInfo.reportType.eq(reportType))
                .fetchFirst() != null;
    }

    @Override
    public ReportResponse getReports() {
        return ReportResponse.of(fetchMemberReports(), fetchPostReports(), fetchReplyReports());
    }

    private List<MemberReportResponse> fetchMemberReports() {
        return queryFactory.select(new QMemberReportResponse(
                        memberReport.id,
                        memberReport.reported,
                        memberReport.loginId,
                        memberReport.reportInfo.reportCategory,
                        memberReport.reportInfo.detailExplanation))
                .from(memberReport)
                .orderBy(memberReport.id.desc())
                .fetch();
    }

    private List<PostReportResponse> fetchPostReports() {
        return queryFactory.select(new QPostReportResponse(
                        postReport.id,
                        postReport.reported,
                        postReport.board,
                        postReport.postId,
                        postReport.reportInfo.reportCategory,
                        postReport.reportInfo.detailExplanation))
                .from(postReport)
                .orderBy(postReport.id.desc())
                .fetch();
    }

    private List<ReplyReportResponse> fetchReplyReports() {
        return queryFactory.select(new QReplyReportResponse(
                        replyReport.id,
                        replyReport.reported,
                        replyReport.postId,
                        replyReport.replyId,
                        replyReport.reportInfo.reportCategory,
                        replyReport.reportInfo.detailExplanation))
                .from(replyReport)
                .orderBy(replyReport.id.desc())
                .fetch();
    }
}
