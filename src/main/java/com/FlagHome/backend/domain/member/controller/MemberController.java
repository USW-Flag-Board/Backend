package com.FlagHome.backend.domain.member.controller;

import com.FlagHome.backend.domain.member.dto.*;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.global.utility.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "아이디 찾기", description = "존재하는 아이디인지 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "422", description = "수원대학교 웹 메일 주소가 아닙니다.")
    })
    @PostMapping("/find-id")
    public ResponseEntity<Void> findId(@RequestBody
                                           @Parameter(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr") String email) {
        memberService.findLoginId(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "아이디 찾기 결과 메일 전송", description = "저장된 유저 아이디를 메일로 전송한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메일 발송 성공"),
            @ApiResponse(responseCode = "500", description = "AWS 서버 에러")
    })
    @PostMapping("/id-result")
    public ResponseEntity<Void> sendFindIdResult(@RequestBody
                                                     @Parameter(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr") String email) {
        memberService.sendFindIdResult(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "비밀번호 재발급 받기", description = "새 비밀번호 전송을 위해 아이디와 이메일 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "422", description = "수원대학교 웹 메일 주소가 아닙니다.")
    })
    @PostMapping("/reissue-password")
    public ResponseEntity<Void> reissuePassword(@RequestBody ReissuePasswordRequest reissuePasswordRequest) {
        memberService.reissuePassword(reissuePasswordRequest.getLoginId(),
                reissuePasswordRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "새 비밀번호 메일 전송", description = "새 비밀번호 전송 후 비밀번호 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메일 발송 성공"),
            @ApiResponse(responseCode = "500", description = "AWS 서버 에러")
    })
    @PostMapping("/new-password")
    public ResponseEntity<Void> sendNewPassword(@RequestBody String email) {
        memberService.sendNewPassword(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "비밀번호 수정", description = "로그인한 유저가 직접 변경하는 경우")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 수정 성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "409", description = "기존과 같은 비밀번호는 사용할 수 없습니다.")
    })
    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        memberService.updatePassword(SecurityUtils.getMemberId(), updatePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "409", description = "비밀번호가 일치하지 않습니다."),
    })
    @DeleteMapping()
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        memberService.withdraw(SecurityUtils.getMemberId(), withdrawRequest.getPassword());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "프로필 업데이트")
    @ApiResponse(responseCode = "200", description = "프로필 업데이트 성공")
    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest) {
        memberService.updateProfile(SecurityUtils.getMemberId(), updateProfileRequest);
        return ResponseEntity.ok().build();
    }
}