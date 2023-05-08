package com.Flaground.backend.module.admin.controller;

import com.Flaground.backend.module.admin.service.AdminService;
import com.Flaground.backend.module.admin.controller.dto.ApproveSignUpResponse;
import com.Flaground.backend.global.common.ApplicationResponse;
import com.Flaground.backend.module.member.controller.dto.response.LoginLogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "admin", description = "관리자 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Tag(name = "admin")
    @Operation(summary = "가입 승인이 필요한 리스트 가져오기", description = "재학생 인증을 한 동아리원 가입요청 리스트 가져오기")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "리스트를 성공적으로 가져왔습니다."),
    })
    @ResponseStatus(OK)
    @GetMapping("/crews")
    public ApplicationResponse<List<ApproveSignUpResponse>> getAllNeedApprovals() {
        return new ApplicationResponse(adminService.getAllAuthorizedAuthMember());
    }

    @Tag(name = "admin")
    @Operation(summary = "동아리원 가입 요청 승인", description = "")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "동아리원 가입을 승인했습니다."),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 가입정보입니다.")
    })
    @ResponseStatus(OK)
    @PostMapping("/crews/{id}")
    public ApplicationResponse approveMember(@PathVariable("id") long authInformationId) {
        adminService.approveMember(authInformationId);
        return new ApplicationResponse();
    }

    @Tag(name = "admin")
    @Operation(summary = "동아리 가입 요청 삭제", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "가입 요청을 삭제했습니다.")
    })
    @ResponseStatus(OK)
    @DeleteMapping("/crews/{id}")
    public ApplicationResponse deleteAuthInformation(@PathVariable("id") long authInformationId) {
        adminService.deleteAuthInformation(authInformationId);
        return new ApplicationResponse();
    }

    @Tag(name = "admin")
    @Operation(summary = "로그 보기", description = "모든 회원들의 아이디와 이름과 마지막 로그인 시간을 볼 수 있는 로그 보기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그 가져오기 성공")
    })
    @ResponseStatus(OK)
    @GetMapping("/member/login-log")
    public ApplicationResponse<List<LoginLogResponse>> viewAllLoginLog() {
        List<LoginLogResponse> responses = adminService.viewAllLoginLogs();
        return new ApplicationResponse(responses);
    }
}