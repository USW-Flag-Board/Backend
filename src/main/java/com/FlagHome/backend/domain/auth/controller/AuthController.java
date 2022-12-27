package com.FlagHome.backend.domain.auth.controller;

import com.FlagHome.backend.domain.auth.dto.*;
import com.FlagHome.backend.domain.auth.service.AuthService;
import com.FlagHome.backend.domain.token.dto.TokenRequest;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/check-id")
    @ApiOperation(value = "아이디 중복 체크")
    public ResponseEntity<Void> checkId(@RequestBody CheckIdRequest idCheckRequest) {
        authService.validateDuplicateLoginId(idCheckRequest.getLoginId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-email")
    @ApiOperation(value = "이메일 중복 체크", notes = "이메일 하나만 하나의 계정만 생성 가능")
    public ResponseEntity<Void> checkEmail(@RequestBody CheckEmailRequest emailCheckRequest) {
        authService.validateEmail(emailCheckRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/join")
    @ApiOperation(value = "회원 정보 입력", notes = "재학생 인증하기 전 정보 입력 단계")
    public ResponseEntity<JoinResponse> join(@RequestBody JoinRequest joinRequest) {
        return ResponseEntity.ok(authService.join(joinRequest));
    }

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "메일 인증 완료 시 회원가입 처리, 가입 구분에 따라 다름")
    public ResponseEntity<SignUpResponse> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/reissue")
    @ApiOperation(value = "JWT 재발급")
    public ResponseEntity<TokenResponse> reissue(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.reissueToken(tokenRequest));
    }
}
