package com.FlagHome.backend.domain.admin.controller;

import com.FlagHome.backend.domain.admin.dto.ApproveSignUpRequest;
import com.FlagHome.backend.domain.admin.service.AdminService;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.common.ApplicationResponse;
import com.FlagHome.backend.domain.member.dto.LoginLogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "admin", description = "관리자 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Tag(name = "admin")
    @Operation(summary = "가입 승인이 필요한 리스트 가져오기", description = "재학생 메일 인증을 수행한 동아리원 가입 요청 리스트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리스트 가져오기 성공"),
    })
    @GetMapping("/approvals")
    public ResponseEntity<List<AuthInformation>> getAllNeedApprovals() {
        return ResponseEntity.ok(adminService.getAllAuthorizedAuthMember());
    }

    @Tag(name = "admin")
    @Operation(summary = "동아리원 가입 승인", description = "")
    @PostMapping("/approval")
    public ResponseEntity<Void> approveMember(@RequestBody ApproveSignUpRequest approveSignUpRequest) {
        adminService.approveMember(approveSignUpRequest.getId());
        return ResponseEntity.ok().build();
    }

    @Tag(name = "admin")
    @Operation(summary = "유저 추방", description = "관리자 권한으로 유저 추방시키기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 추방 성공")
    })
    @DeleteMapping("/member/{member-id}")
    public ResponseEntity<Void> withdrawMember(@PathVariable("member-id") long memberId) {
        adminService.withdrawMember(memberId);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "admin")
    @Operation(summary = "인증 정보 삭제", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 가입 정보 삭제")
    })
    @DeleteMapping("/{auth-id}")
    public ResponseEntity<Void> deleteAuthInformation(@PathVariable("auth-id") long authInformationId) {
        adminService.deleteAuthInformation(authInformationId);
        return ResponseEntity.ok().build();
    }

    //로그 가져오기
    @Tag(name = "admin")
    @Operation(summary = "로그 보기", description = "모든 회원들의 아이디와 이름과 마지막 로그인 시간을 볼 수 있는 로그 보기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그 가져오기 성공")
    })
    @ResponseStatus(OK)
    @GetMapping("/member/login-log")
    public ApplicationResponse<List<LoginLogResponse>> viewAllLoginLog() {
        List<LoginLogResponse> responses = adminService.viewAllLoginLog();
        return new ApplicationResponse(responses);
    }
}