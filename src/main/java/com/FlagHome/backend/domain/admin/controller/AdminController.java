package com.FlagHome.backend.domain.admin.controller;

import com.FlagHome.backend.domain.admin.dto.ApproveSignUpRequest;
import com.FlagHome.backend.domain.admin.service.AdminService;
import com.FlagHome.backend.domain.auth.entity.AuthMember;
import com.FlagHome.backend.domain.auth.service.AuthMemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/approval")
    @ApiOperation(value = "가입 승인이 필요한 리스트 가져오기", notes = "재학생 메일 인증을 수행한 동아리원 가입 요청 리스트")
    public ResponseEntity<List<AuthMember>> getAllNeedApprovals() {
        return ResponseEntity.ok(adminService.getAllAuthorizedAuthMember());
    }

    @PostMapping("/approval")
    @ApiOperation(value = "회원가입 승인", notes = "동아리원 정보 암호화 후 DB에 저장, 인증 정보 삭제")
    public ResponseEntity<Void> approveAuthMember(@RequestBody ApproveSignUpRequest approveSignUpRequest) {
        adminService.approveAuthMember(approveSignUpRequest.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{auth_Id}")
    @ApiOperation(value = "회원가입 거부", notes = "인증 정보를 삭제")
    public ResponseEntity<Void> deleteAuthMember(@PathVariable(name = "auth_id") long authMemberId) {
        adminService.deleteAuthMember(authMemberId);
        return ResponseEntity.ok().build();
    }
}