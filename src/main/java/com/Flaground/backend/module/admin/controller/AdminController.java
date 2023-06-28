package com.Flaground.backend.module.admin.controller;

import com.Flaground.backend.global.common.response.ApplicationResponse;
import com.Flaground.backend.global.utility.SecurityUtils;
import com.Flaground.backend.module.admin.controller.dto.CreateNoticeRequest;
import com.Flaground.backend.module.admin.service.AdminService;
import com.Flaground.backend.module.auth.controller.dto.response.SignUpRequestResponse;
import com.Flaground.backend.module.board.controller.dto.request.BoardRequest;
import com.Flaground.backend.module.member.controller.dto.response.LoginLogResponse;
import com.Flaground.backend.module.report.controller.dto.response.ReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "admin", description = "관리자 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Tag(name = "admin")
    @Operation(summary = "동아리 회원가입 요청 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이상 없이 처리"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다.")
    })
    @ResponseStatus(OK)
    @GetMapping("/join-requests")
    public ApplicationResponse<List<SignUpRequestResponse>> getSignUpRequests() {
        List<SignUpRequestResponse> responses = adminService.getSignUpRequests();
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "admin")
    @Operation(summary = "유저 로그인 이력 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이상 없이 처리"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다.")
    })
    @ResponseStatus(OK)
    @GetMapping("/members/login-log")
    public ApplicationResponse<List<LoginLogResponse>> getLoginLogs() {
        List<LoginLogResponse> responses = adminService.getLoginLogs();
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "admin")
    @Operation(summary = "신고 목록 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이상 없이 처리"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다.")
    })
    @ResponseStatus(OK)
    @GetMapping("/reports")
    public ApplicationResponse<ReportResponse> getReports() {
        ReportResponse responses = adminService.getReports();
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "admin")
    @Operation(summary = "동아리 회원가입 요청 승인하기", description = "일반 회원은 자동으로 회원가입 처리가 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "이상 없이 가입 처리"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 가입정보입니다.")
    })
    @ResponseStatus(CREATED)
    @PostMapping("/join-requests/{id}/approval")
    public ApplicationResponse approveSignUp(@PathVariable("id") Long authInformationId) {
        adminService.approveSignUp(authInformationId);
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @Operation(summary = "게시판 생성하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 생성 완료"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다."),
            @ApiResponse(responseCode = "409", description = "이미 같은 게시판이 존재합니다.")
    })
    @ResponseStatus(CREATED)
    @PostMapping("/boards")
    public ApplicationResponse create(@RequestBody @Valid BoardRequest boardRequest) {
        adminService.createBoard(boardRequest.toEntity());
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @Operation(summary = "공지사항 작성하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 작성 성공"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다."),
    })
    @ResponseStatus(CREATED)
    @PostMapping("/posts/notice")
    public ApplicationResponse createNotice(@RequestBody @Valid CreateNoticeRequest request) {
        adminService.createNotice(SecurityUtils.getMemberId(), request.getTitle(), request.getContent());
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @Operation(summary = "게시판 수정하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 수정 완료"),
            @ApiResponse(responseCode = "400", description = "올바르지 않는 게시판입니다."),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다."),
            @ApiResponse(responseCode = "409", description = "이미 같은 게시판이 존재합니다.")
    })
    @ResponseStatus(OK)
    @PutMapping("/boards/{name}")
    public ApplicationResponse update(@PathVariable("name") String boardName,
                                      @RequestBody @Valid BoardRequest boardRequest) {
        adminService.updateBoard(boardName, boardRequest.toEntity());
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @Operation(summary = "동아리 회원가입 요청 거절하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이상 없이 처리완료"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다."),
    })
    @ResponseStatus(OK)
    @DeleteMapping("/join-requests/{id}/rejection")
    public ApplicationResponse rejectSignUp(@PathVariable("id") Long authInformationId) {
        adminService.rejectSignUp(authInformationId);
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @Operation(summary = "게시판 삭제하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판 삭제 완료"),
            @ApiResponse(responseCode = "400", description = "올바르지 않는 게시판입니다."),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다.")
    })
    @ResponseStatus(OK)
    @DeleteMapping("/boards/{name}")
    public ApplicationResponse delete(@PathVariable("name") String boardName) {
        adminService.deleteBoard(boardName);
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @Operation(summary = "신고 요청 처리하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이상 없이 처리완료"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다."),
    })
    @ResponseStatus(OK)
    @DeleteMapping("/reports/handle/{id}")
    public ApplicationResponse handleReport(@PathVariable("id") Long reportId) {
        String message = adminService.dealReport(reportId);
        return new ApplicationResponse<>(message);
    }

    @Tag(name = "admin")
    @Operation(summary = "모든 신고 요청 삭제하기", description = "모든 요청을 삭제한다. 주의하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이상 없이 처리완료"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다."),
    })
    @ResponseStatus(OK)
    @DeleteMapping("/reports")
    public ApplicationResponse deleteAllReports() {
        adminService.ignoreAllReports();
        return new ApplicationResponse<>();
    }

    @Tag(name = "admin")
    @Operation(summary = "신고 요청 삭제하기", description = "하나만 삭제한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이상 없이 처리완료"),
            @ApiResponse(responseCode = "401", description = "관리자가 아닙니다."),
    })
    @ResponseStatus(OK)
    @DeleteMapping("/reports/{id}")
    public ApplicationResponse deleteReport(@PathVariable("id") Long reportId) {
        adminService.ignoreReport(reportId);
        return new ApplicationResponse<>();
    }
}
