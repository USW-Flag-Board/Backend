package com.FlagHome.backend.domain.report.repository;

import com.FlagHome.backend.domain.report.controller.dto.QReportResponse;
import com.FlagHome.backend.domain.report.controller.dto.ReportResponse;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.FlagHome.backend.domain.member.QMember.member;
import static com.FlagHome.backend.domain.report.QReport.report;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {
    private final JPQLQueryFactory queryFactory;

    @Override
    public boolean existsByMemberIdAndUrl(Long memberId, String url) {
        return queryFactory
                .selectFrom(report)
                .where(report.reporter.id.eq(memberId),
                        report.reportedURL.eq(url))
                .fetchFirst() != null;
    }

    @Override
    public List<ReportResponse> getAllReports() {
        return queryFactory
                .select(new QReportResponse(
                        report.id,
                        member.name,
                        report.reportType,
                        report.detailReason,
                        report.reportedURL))
                .from(report)
                .innerJoin(report.reported, member)
                .fetch();
    }
}
