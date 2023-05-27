package com.Flaground.backend.module.report.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReport extends Report {
    @Column
    private String board;

    @Column(name = "post_id")
    private Long postId;

    @Builder
    public PostReport(Long reporter, Long reported, ReportInfo reportInfo, String board, Long postId) {
        super(reporter, reported, reportInfo);
        this.board = board;
        this.postId = postId;
    }

    public static PostReport of(Long reporter, Long reported,
                                String board , AbstractReport<Long> abstractReport) {
        return PostReport.builder()
                .reporter(reporter)
                .reported(reported)
                .board(board)
                .postId(abstractReport.getTarget())
                .reportInfo(ReportInfo.from(abstractReport))
                .build();
    }
}
