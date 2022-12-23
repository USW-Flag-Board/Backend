package com.FlagHome.backend.v1.member.controller;

import com.FlagHome.backend.v1.member.dto.*;
import com.FlagHome.backend.v1.member.mail.MailService;
import com.FlagHome.backend.v1.member.mail.MailType;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.v1.member.service.MemberService;
import com.FlagHome.backend.v1.util.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/check-id")
    @ApiOperation(value = "아이디 중복 체크")
    public ResponseEntity<Void> checkId(@RequestBody IdCheckRequest idCheckRequest) {
        memberService.validateDuplicateLoginId(idCheckRequest.getLoginId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-email")
    @ApiOperation(value = "이메일 중복 체크", notes = "이메일 하나만 하나의 계정만 생성 가능")
    public ResponseEntity<Void> checkEmail(@RequestBody EmailCheckRequest emailCheckRequest) {
        memberService.validateEmail(emailCheckRequest.getEmail());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    @ApiOperation(value = "회원 탈퇴")
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        memberService.withdrawMember(withdrawRequest.getPassword());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/find-id")
    @ApiOperation(value = "아이디 찾기", notes = "요청 시 아이디를 찾아서 메일로 전송")
    public ResponseEntity<Void> findId(@RequestBody FindIdRequest findIdRequest) {
        memberService.sendFindIdResult(findIdRequest.getName(), findIdRequest.getEmail());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/find-password")
    @ApiOperation(value = "비밀번호 찾기", notes = "비밀번호 찾기 요청 시 새로운 비밀번호 발급 후 재설정")
    public ResponseEntity<Void> findPassword(@RequestBody FindPasswordRequest findPasswordRequest) {
        String newPassword = memberService
                .updatePassword(findPasswordRequest.getLoginId(), findPasswordRequest.getEmail());

        memberService.sendFindPasswordResult(findPasswordRequest.getEmail(), newPassword);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile")
    @ApiOperation(value = "프로필 업데이트")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest) {
        memberService.updateProfile(SecurityUtils.getMemberId(), updateProfileRequest);
        return ResponseEntity.ok().build();
    }
}