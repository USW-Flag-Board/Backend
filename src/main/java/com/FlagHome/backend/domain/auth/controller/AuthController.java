package com.FlagHome.backend.domain.auth.controller;

import com.FlagHome.backend.domain.common.ApplicationResponse;
import com.FlagHome.backend.domain.auth.dto.*;
import com.FlagHome.backend.domain.auth.service.AuthService;
import com.FlagHome.backend.domain.token.dto.TokenRequest;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Tag(name = "auth")
    @Operation(summary = "아이디 중복 체크")
    @ApiResponse(responseCode = "200", description = "중복 체크 성공. False : 사용 가능, True : 사용 불가능")
    @ResponseStatus(OK)
    @PostMapping("/check/id")
    public ApplicationResponse<Boolean> checkId(@RequestBody CheckLoginIdRequest checkLoginIdRequest) {
        boolean check = authService.validateDuplicateLoginId(checkLoginIdRequest.getLoginId());
        return new ApplicationResponse(check);
    }

    @Tag(name = "auth")
    @Operation(summary = "이메일 중복 체크", description = "하나의 이메일에 하나의 계정만 생성 가능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "중복 체크 성공. False : 사용 가능, True : 사용 불가능"),
            @ApiResponse(responseCode = "400", description = "이메일 형식이 아닙니다."),
            @ApiResponse(responseCode = "422", description = "수원대학교 웹 메일 주소가 아닙니다.")
    })
    @ResponseStatus(OK)
    @PostMapping("/check/email")
    public ApplicationResponse<Boolean> checkEmail(@RequestBody CheckEmailRequest checkEmailRequest) {
        boolean check = authService.validateEmail(checkEmailRequest.getEmail());
        return new ApplicationResponse(check);
    }

    @Tag(name = "auth")
    @Operation(summary = "회원 정보 입력 및 메일 전송", description = "작성한 데이터 검사 후 인증정보 저장 및 메일 전송" +
            "\n저장한 정보는 관리자만 볼 수 있어서 URI 리턴 없음.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원정보 입력 성공 및 메일 발송 완료"),
            @ApiResponse(responseCode = "422", description = "사용할 수 없는 비밀번호 입니다. (8~20자 이내 영문, 숫자, 특수문자를 모두 포함)"),
            @ApiResponse(responseCode = "500", description = "서버 에러입니다. 관리자에게 문의해주세요.")
    })
    @ResponseStatus(OK)
    @PostMapping("/join")
    public ApplicationResponse<JoinResponse> join(@RequestBody JoinRequest joinRequest) {
        JoinResponse response = authService.join(joinRequest);
        return new ApplicationResponse(response);
    }

    @Tag(name = "auth")
    @Operation(summary = "회원가입", description = "재학생 인증 완료 시 회원가입, 동아리원은 추가 관리자 인증 필요")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공에 성공했습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 가입정보입니다."),
            @ApiResponse(responseCode = "409", description = "인증번호가 일치하지 않습니다."),
    })
    @ResponseStatus(OK)
    @PostMapping("/sign-up")
    public ApplicationResponse<SignUpResponse> signup(@RequestBody SignUpRequest signUpRequest) {
        SignUpResponse response = authService.signUp(signUpRequest.getEmail(), signUpRequest.getCertification());
        return new ApplicationResponse(response);
    }

    @Tag(name = "auth")
    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 발급"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @ResponseStatus(OK)
    @PostMapping("/login")
    public ApplicationResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse response = authService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        return new ApplicationResponse(response);
    }

    @Tag(name = "auth")
    @Operation(summary = "JWT 재발급")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "JWT 토큰 재발급 성공"),
        @ApiResponse(responseCode = "400", description = "유효하지 않는 토큰입니다."),
        @ApiResponse(responseCode = "401", description = "권한이 없는 토큰입니다."),
        @ApiResponse(responseCode = "409", description = "토큰의 정보가 일치하지 않습니다.")
    })
    @ResponseStatus(OK)
    @PostMapping("/reissue")
    public ApplicationResponse<TokenResponse> reissue(@RequestBody TokenRequest tokenRequest) {
        TokenResponse response = authService.reissueToken(tokenRequest.getAccessToken(), tokenRequest.getRefreshToken());
        return new ApplicationResponse(response);
    }
}
