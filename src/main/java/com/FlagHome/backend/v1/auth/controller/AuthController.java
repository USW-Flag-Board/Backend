package com.FlagHome.backend.v1.auth.controller;

import com.FlagHome.backend.v1.auth.dto.LogInRequest;
import com.FlagHome.backend.v1.auth.dto.SignUpRequest;
import com.FlagHome.backend.v1.auth.dto.SignUpResponse;
import com.FlagHome.backend.v1.auth.service.AuthService;
import com.FlagHome.backend.v1.token.dto.TokenResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public ResponseEntity<TokenResponse> logIn(@RequestBody LogInRequest logInRequest) {
        return ResponseEntity.ok(authService.logIn(logInRequest));
    }

}
