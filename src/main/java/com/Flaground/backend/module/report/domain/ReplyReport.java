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
public class ReplyReport extends Report {
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "reply_id")
    private Long replyId;

    @Builder
    public ReplyReport(Long reporter, Long reported, ReportInfo reportInfo, Long postId, Long replyId) {
        super(reporter, reported, reportInfo);
        this.postId = postId;
        this.replyId = replyId;
    }

    public static ReplyReport of(Long reporter, Long reported, Long postId,
                                 AbstractReport<Long> abstractReport) {
        return ReplyReport.builder()
                .reporter(reporter)
                .reported(reported)
                .postId(postId)
                .replyId(abstractReport.getTarget())
                .reportInfo(ReportInfo.from(abstractReport))
                .build();
    }
}
