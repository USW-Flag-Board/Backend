package com.FlagHome.backend.domain.member.controller;

import com.FlagHome.backend.domain.HttpResponse;
import com.FlagHome.backend.domain.member.dto.*;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.global.utility.SecurityUtils;
import com.FlagHome.backend.global.utility.UriCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "member", description = "멤버 API")
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/api/member";
    private final MemberService memberService;

    @Tag(name = "member")
    @Operation(summary = "유저 확인", description = "아이디/비밀번호 찾기 전 유저 확인, parameter에 따라 다르게 동작한다.\n" +
                                                    "아이디 찾기 : 이메일 입력 / 비밀번호 재발급 : 이메일, 아이디 입력")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "존재하는 사용자 입니다."),
            @ApiResponse(responseCode = "400", description = "이메일을 입력하지 않은 경우 400 Error 발생"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "422", description = "수원대학교 웹 메일 주소가 아닙니다.")
    })
    @GetMapping()
    public ResponseEntity<Void> findMember(@RequestParam(value = "id", required = false) String loginId,
                                           @RequestParam String email) {
        memberService.isMemberExist(loginId, email);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "member")
    @Operation(summary = "멤버 프로필 가져오기", description = "아직 미완성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<MyPageResponse> getMemberPage(@PathVariable("id") String loginId) {
        return ResponseEntity.ok(memberService.getMyPage(loginId));
    }

    @Tag(name = "member")
    @Operation(summary = "아이디 찾기 결과 메일 전송", description = "저장된 유저 아이디를 메일로 전송한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "아이디 찾기 결과 메일 발송 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러입니다. 관리자에게 문의해주세요.")
    })
    @PostMapping("/mail/id")
    public ResponseEntity<HttpResponse> sendFindIdResult(@RequestBody SendEmailRequest sendEmailRequest) {
        HttpResponse response = HttpResponse
                .ok(memberService.sendFindIdResult(sendEmailRequest.getEmail()), OK, "아이디 찾기 결과 메일 발송 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "member")
    @Operation(summary = "새 비밀번호 메일 전송", description = "새 비밀번호 전송 후 비밀번호 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "새 비밀번호 메일 발송 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러입니다. 관리자에게 문의해주세요.")
    })
    @PostMapping("/mail/password")
    public ResponseEntity<HttpResponse> sendNewPassword(@RequestBody SendEmailRequest sendEmailRequest) {
        HttpResponse response = HttpResponse
                .ok(memberService.sendNewPassword(sendEmailRequest.getEmail()), OK, "새 비밀번호 메일 발송 성공");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "member")
    @Operation(summary = "비밀번호 수정", description = "로그인한 유저가 직접 변경하는 경우")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 수정 성공, 유저 URI 리턴"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "409", description = "기존과 같은 비밀번호는 사용할 수 없습니다.")
    })
    @PatchMapping("/password")
    public ResponseEntity<HttpResponse> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        String loginId = memberService.updatePassword(SecurityUtils.getMemberId(), updatePasswordRequest);
        URI location = UriCreator.createMemberUri(MEMBER_DEFAULT_URL, loginId);
        HttpResponse response = HttpResponse.ok(location, OK, "비밀번호 변경에 성공했습니다.");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "member")
    @Operation(summary = "프로필 업데이트")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "비밀번호 업데이트 성공, 유저 URI 리턴"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @PatchMapping("/profile")
    public ResponseEntity<HttpResponse> updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest) {
        String loginId = memberService.updateProfile(SecurityUtils.getMemberId(), updateProfileRequest);
        URI location = UriCreator.createMemberUri(MEMBER_DEFAULT_URL, loginId);
        HttpResponse response = HttpResponse.ok(location, OK, "비밀번호 변경에 성공했습니다.");
        return ResponseEntity.ok(response);
    }

    @Tag(name = "member")
    @Operation(summary = "회원 탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @DeleteMapping()
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        memberService.withdraw(SecurityUtils.getMemberId(), withdrawRequest.getPassword());
        return ResponseEntity.ok().build();
    }
}