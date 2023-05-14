package com.Flaground.backend.module.admin.service;

import com.Flaground.backend.module.auth.controller.dto.response.SignUpRequestResponse;
import com.Flaground.backend.module.auth.domain.AuthInformation;
import com.Flaground.backend.module.auth.service.AuthService;
import com.Flaground.backend.module.board.domain.Board;
import com.Flaground.backend.module.board.service.BoardService;
import com.Flaground.backend.module.member.controller.dto.response.LoginLogResponse;
import com.Flaground.backend.module.member.service.MemberService;
import com.Flaground.backend.module.report.controller.dto.response.ReportResponse;
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

    @Transactional(readOnly = true)
    public List<SignUpRequestResponse> getSignUpRequests() {
        return authService.getSignUpRequests();
    }

    @Transactional(readOnly = true)
    public List<LoginLogResponse> getLoginLogs() {
        return memberService.getLoginLogs();
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getReports() {
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

    public void updateBoard(String boardName, Board board) {
        boardService.update(boardName, board);
    }

    public void deleteBoard(String boardName) {
        boardService.delete(boardName);
    }
}
