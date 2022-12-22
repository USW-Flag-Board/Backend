package com.FlagHome.backend.v1.member.controller;

import com.FlagHome.backend.global.util.SecurityUtils;
import com.FlagHome.backend.v1.member.dto.*;
import com.FlagHome.backend.v1.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/check-id")
    @ApiOperation(value = "아이디 중복 체크")
    public ResponseEntity<Void> checkId(@RequestBody CheckIdRequest idCheckRequest) {
        memberService.validateDuplicateLoginId(idCheckRequest.getLoginId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-email")
    @ApiOperation(value = "이메일 중복 체크", notes = "이메일 하나만 하나의 계정만 생성 가능")
    public ResponseEntity<Void> checkEmail(@RequestBody CheckEmailRequest emailCheckRequest) {
        memberService.validateEmail(emailCheckRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/withdraw")
    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴 시 탈퇴 상태로만 변경")
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        memberService.withdraw(SecurityUtils.getMemberId(), withdrawRequest.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/find-id")
    @ApiOperation(value = "아이디 찾기", notes = "요청 시 아이디를 찾아서 메일로 전송")
    public ResponseEntity<Void> findId(@RequestBody FindIdRequest findIdRequest) {
        memberService.findLoginId(findIdRequest.getName(), findIdRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue-password")
    @ApiOperation(value = "비밀번호 재발급", notes = "비밀번호 찾기 요청 시 새로운 비밀번호 발급 후 재설정")
    public ResponseEntity<Void> reissuePassword(@RequestBody ReissuePasswordRequest reissuePasswordRequest) {
        memberService.reissuePassword(reissuePasswordRequest.getLoginId(),
                reissuePasswordRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    @ApiOperation(value = "비밀번호 수정")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        memberService.updatePassword(SecurityUtils.getMemberId(), updatePasswordRequest);
        return ResponseEntity.ok().build();
    }
}