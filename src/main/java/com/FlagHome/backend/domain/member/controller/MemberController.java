package com.FlagHome.backend.domain.member.controller;

import com.FlagHome.backend.domain.ApplicationResponse;
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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "member", description = "멤버 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/api/members";
    private final MemberService memberService;

    @Tag(name = "member")
    @Operation(summary = "멤버 프로필 가져오기", description = "아직 미완성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @GetMapping("/{id}")
    public ResponseEntity<MemberProfileResponse> getMemberPage(@PathVariable("id") String loginId) {
        return ResponseEntity.ok(memberService.getMemberProfile(loginId));
    }

    @Tag(name = "member")
    @Operation(summary = "아이디 찾기", description = "존재하는 멤버라면 인증 이메일 발송\n" +
                                                    "201이 뜬다면 인증 진행하기")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "조회에 성공해 메일을 발송합니다."),
            @ApiResponse(responseCode = "400", description = "이메일 형식이 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "422", description = "수원대학교 웹 메일 주소가 아닙니다.")
    })
    @PostMapping("/find/id")
    public ResponseEntity<ApplicationResponse> findId(@RequestBody FindIdRequest findIdRequest) {
        return ResponseEntity.ok(ApplicationResponse
                .of(memberService.findId(findIdRequest.getEmail()), CREATED, "멤버 조회에 성공해 메일을 발송합니다."));
    }

    @Tag(name = "member")
    @Operation(summary = "비밀번호 찾기(바꾸기)", description = "존재하는 멤버라면 인증 이메일 발송\n" +
                                                            "201이 뜬다면 인증 진행하기")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "조회에 성공해 메일을 발송합니다."),
            @ApiResponse(responseCode = "400", description = "이메일 형식이 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "422", description = "수원대학교 웹 메일 주소가 아닙니다.")
    })
    @PostMapping("/find/password")
    public ResponseEntity<ApplicationResponse> findPassword(@RequestBody FindPasswordRequest findPasswordRequest) {
        return ResponseEntity.ok(ApplicationResponse
                .of(memberService.findPassword(findPasswordRequest.getLoginId(), findPasswordRequest.getEmail()), CREATED, "멤버 조회에 성공해 메일을 발송합니다."));
    }

    @Tag(name = "member")
    @Operation(summary = "인증번호 인증하기", description = "아이디/비밀번호 찾기 이후 인증 단계")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "유효하지 않는 토큰입니다."),
            @ApiResponse(responseCode = "404", description = "아이디/비밀번호 찾기 요청이 존재하지 않습니다."),
            @ApiResponse(responseCode = "409", description = "인증번호가 일치하지 않습니다.")
    })
    @PostMapping("/certification")
    public ResponseEntity<ApplicationResponse> authCertification(@RequestBody AuthenticationRequest authenticationRequest) {
        memberService.validateCertification(authenticationRequest.getEmail(), authenticationRequest.getCertification());
        return ResponseEntity.ok(ApplicationResponse.of(null, OK, "인증에 성공했습니다."));
    }

    @Tag(name = "member")
    @Operation(summary = "비밀번호 수정", description = "비밀번호 찾기로 새로운 비밀번호를 설정하는 경우")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호를 변경했습니다. 로그인 해주세요."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "422", description = "사용할 수 없는 비밀번호 입니다. (8~20자 이내 영문, 숫자, 특수문자를 모두 포함)")
    })
    @PutMapping("/find/password")
    public ResponseEntity<ApplicationResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        memberService.changePassword(changePasswordRequest.getEmail(), changePasswordRequest.getNewPassword());
        return ResponseEntity.ok(ApplicationResponse.of(null, OK, "비밀번호를 변경했습니다. 로그인 해주세요."));
    }

    @Tag(name = "member")
    @Operation(summary = "비밀번호 수정", description = "로그인한 유저가 직접 비밀번호를 변경하는 경우\n" +
                                                      "유저 프로필로 이동한다. (URI 리턴)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 수정 성공, 유저 URI 리턴"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "409", description = "기존과 같은 비밀번호는 사용할 수 없습니다."),
            @ApiResponse(responseCode = "422", description = "사용할 수 없는 비밀번호 입니다. (8~20자 이내 영문, 숫자, 특수문자를 모두 포함)")
    })
    @PutMapping("/password")
    public ResponseEntity<ApplicationResponse> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        String loginId = memberService.updatePassword(SecurityUtils.getMemberId(), updatePasswordRequest);
        URI location = UriCreator.createMemberUri(MEMBER_DEFAULT_URL, loginId);
        return ResponseEntity.ok(ApplicationResponse.of(location, OK, "비밀번호 변경에 성공했습니다."));
    }

    @Tag(name = "member")
    @Operation(summary = "회원 탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.")
    })
    @DeleteMapping()
    public ResponseEntity<ApplicationResponse> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        memberService.withdraw(SecurityUtils.getMemberId(), withdrawRequest.getPassword());
        return ResponseEntity.ok(ApplicationResponse.of(null, OK, "회원 탈퇴에 성공하였습니다."));
    }
}