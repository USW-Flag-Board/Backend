package com.Flaground.backend.module.report.service;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.service.MemberService;
import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.domain.Reply;
import com.Flaground.backend.module.post.service.PostService;
import com.Flaground.backend.module.post.service.ReplyService;
import com.Flaground.backend.module.report.controller.dto.response.ReportResponse;
import com.Flaground.backend.module.report.domain.*;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import com.Flaground.backend.module.report.domain.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberService memberService;
    private final PostService postService;
    private final ReplyService replyService;

    @Transactional(readOnly = true)
    public ReportResponse getReports() {
        return reportRepository.getReports();
    }

    public void reportMember(Long memberId, AbstractReport<String> abstractReport) {
        Member reported = memberService.findByLoginId(abstractReport.getTarget());
        validateDuplicate(memberId, reported.getId(), abstractReport.getReportType());
        reportRepository.save(MemberReport.of(memberId, reported.getId(), abstractReport));
    }

    public void reportPost(Long memberId, AbstractReport<Long> abstractReport) {
        Post post = postService.findById(abstractReport.getTarget());
        validateDuplicate(memberId, post.getMember().getId(), abstractReport.getReportType());
        reportRepository.save(PostReport.of(memberId, post.getMember().getId(), post.getBoardName(), abstractReport));
    }

    public void reportReply(Long memberId, AbstractReport<Long> abstractReport) {
        Reply reply = replyService.findById(abstractReport.getTarget());
        validateDuplicate(memberId, reply.getMember().getId(), abstractReport.getReportType());
        reportRepository.save(ReplyReport.of(memberId, reply.getMember().getId(), reply.getPostId(), abstractReport));
    }

    public void delete(Long reportId) {
        reportRepository.deleteById(reportId);
    }

    public Report findById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
    }

    private void validateDuplicate(Long reporterId, Long reportedId, ReportType reportType) {
        if (isReported(reporterId, reportedId, reportType)) {
            throw new CustomException(ErrorCode.ALREADY_REPORTED);
        }
    }

    private boolean isReported(Long reporterId, Long reportedId, ReportType reportType) {
        return reportRepository.existByIdsAndType(reporterId, reportedId, reportType);
    }
}
