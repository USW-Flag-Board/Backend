package com.Flaground.backend.module.admin.service;

import com.Flaground.backend.module.auth.controller.dto.response.SignUpRequestResponse;
import com.Flaground.backend.module.auth.domain.AuthInformation;
import com.Flaground.backend.module.auth.service.AuthService;
import com.Flaground.backend.module.board.domain.Board;
import com.Flaground.backend.module.board.service.BoardService;
import com.Flaground.backend.module.member.controller.dto.response.LoginLogResponse;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.service.BlackListService;
import com.Flaground.backend.module.member.service.MemberService;
import com.Flaground.backend.module.post.domain.PostData;
import com.Flaground.backend.module.post.service.PostService;
import com.Flaground.backend.module.report.controller.dto.response.ReportResponse;
import com.Flaground.backend.module.report.domain.Report;
import com.Flaground.backend.module.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final MemberService memberService;
    private final AuthService authService;
    private final BoardService boardService;
    private final ReportService reportService;
    private final PostService postService;
    private final BlackListService blackListService;

    @Transactional(readOnly = true)
    public List<SignUpRequestResponse> getSignUpRequests() {
        return authService.getSignUpRequests();
    }

    @Transactional(readOnly = true)
    public List<LoginLogResponse> getLoginLogs() {
        return memberService.getLoginLogs();
    }

    @Transactional(readOnly = true)
    public ReportResponse getReports() {
        return reportService.getReports();
    }

    public void approveSignUp(Long authInformationId) {
        AuthInformation authInformation = authService.findById(authInformationId);
        memberService.initMember(authInformation.toJoinMember());
        authService.deleteJoinRequest(authInformationId);
    }

    public void rejectSignUp(Long authInformationId) {
        authService.deleteJoinRequest(authInformationId);
    }

    public void createBoard(Board board) {
        boardService.create(board);
    }

    public void createNotice(Long adminId, String title, String content) {
        PostData postData = PostData.notice(title, content);
        postService.create(adminId, postData);
    }

    public void updateBoard(String boardName, Board board) {
        boardService.update(boardName, board);
    }

    public void deleteBoard(String boardName) {
        boardService.delete(boardName);
    }

    public String dealReport(Long reportId) {
        Report report = reportService.findById(reportId);
        Member member = memberService.findById(report.getReported());
        reportService.delete(reportId);
        return blackListService.dealReport(member, report.getPenalty());
    }

    public void ignoreAllReports() {
        reportService.deleteAll();
    }

    public void ignoreReport(Long reportId) {
        reportService.delete(reportId);
    }
}
