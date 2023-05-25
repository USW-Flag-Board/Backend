package com.Flaground.backend.module.report.controller.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ReportResponse {
    private List<MemberReportResponse> memberReportResponses;
    private List<PostReportResponse> postReportResponses;
    private List<ReplyReportResponse> replyReportResponses;

    public static ReportResponse of(List<MemberReportResponse> memberReportResponses,
                                    List<PostReportResponse> postReportResponses,
                                    List<ReplyReportResponse> replyReportResponses) {
        return new ReportResponse(memberReportResponses, postReportResponses, replyReportResponses);
    }
}
