package com.FlagHome.backend.domain.report.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.FlagHome.backend.domain.report.entity.QReport.report;

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
}
