package com.FlagHome.backend.domain.auth.controller;

import com.FlagHome.backend.domain.auth.dto.*;
import com.FlagHome.backend.domain.auth.service.AuthService;
import com.FlagHome.backend.domain.token.dto.TokenRequest;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Tag(name = "auth")
    @Operation(summary = "아이디 중복 체크")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 아이디입니다."),
            @ApiResponse(responseCode = "409", description = "이미 사용 중인 아이디입니다.")
    })
    @GetMapping("/id")
    public ResponseEntity<Void> checkId(@RequestBody CheckLoginIdRequest checkLoginIdRequest) {
        authService.validateDuplicateLoginId(checkLoginIdRequest.getLoginId());
        return ResponseEntity.ok().build();
    }

    @Tag(name = "auth")
    @Operation(summary = "이메일 중복 체크", description = "하나의 이메일에 하나의 계정만 생성 가능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 이메일입니다."),
            @ApiResponse(responseCode = "400", description = "이메일 형식이 아닙니다."),
            @ApiResponse(responseCode = "409", description = "이미 사용 중인 이메일입니다."),
            @ApiResponse(responseCode = "422", description = "수원대학교 웹 메일 주소가 아닙니다.")
    })
    @GetMapping("/email")
    public ResponseEntity<Void> checkEmail(@RequestBody CheckEmailRequest checkEmailRequest) {
        authService.validateEmail(checkEmailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @Tag(name = "auth")
    @Operation(summary = "회원 정보 입력", description = "회원가입 시 작성한 데이터 검사 및 인증정보 저장" +
            "\n저장한 정보는 관리자만 볼 수 있어서 URI 리턴 없음.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "422", description = "사용할 수 없는 비밀번호 입니다. (8~20자 이내 영문, 숫자, 특수문자를 모두 포함)"),
    })
    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody JoinRequest joinRequest) {
        authService.join(joinRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "auth")
    @Operation(summary = "재학생 인증 이메일 전송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재학생 인증 메일 전송 완료"),
            @ApiResponse(responseCode = "500", description = "서버 에러입니다. 관리자에게 문의해주세요.")
    })
    @PostMapping("/email")
    public ResponseEntity<JoinResponse> sendCertificationMail(@RequestBody SendEmailRequest sendEmailRequest) {
        return ResponseEntity.ok(authService.sendCertification(sendEmailRequest.getEmail()));
    }

    @Tag(name = "auth")
    @Operation(summary = "회원가입", description = "재학생 인증 완료 시 회원가입, 동아리원은 추가 관리자 인증 필요")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 가입정보입니다."),
            @ApiResponse(responseCode = "409", description = "인증번호가 일치하지 않습니다."),
    })
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @Tag(name = "auth")
    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 발급"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Tag(name = "auth")
    @Operation(summary = "JWT 재발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "JWT 토큰 재발급 성공")
    })
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.reissueToken(tokenRequest));
    }
}
