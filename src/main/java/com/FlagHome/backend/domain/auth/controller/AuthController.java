package com.FlagHome.backend.domain.auth.controller;

import com.FlagHome.backend.domain.common.ApiResponse;
import com.FlagHome.backend.domain.auth.dto.*;
import com.FlagHome.backend.domain.auth.service.AuthService;
import com.FlagHome.backend.domain.token.dto.TokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용 가능한 아이디입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 사용 중인 아이디입니다.")
    })
    @ResponseStatus(OK)
    @PostMapping("/check/id")
    public ApiResponse checkId(@RequestBody CheckLoginIdRequest checkLoginIdRequest) {
        authService.validateDuplicateLoginId(checkLoginIdRequest.getLoginId());
        return new ApiResponse();
    }

    @Tag(name = "auth")
    @Operation(summary = "이메일 중복 체크", description = "하나의 이메일에 하나의 계정만 생성 가능")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용 가능한 이메일입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이메일 형식이 아닙니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 사용 중인 이메일입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "수원대학교 웹 메일 주소가 아닙니다.")
    })
    @ResponseStatus(OK)
    @PostMapping("/check/email")
    public ApiResponse checkEmail(@RequestBody CheckEmailRequest checkEmailRequest) {
        authService.validateEmail(checkEmailRequest.getEmail());
        return new ApiResponse();
    }

    @Tag(name = "auth")
    @Operation(summary = "회원 정보 입력 및 메일 전송", description = "작성한 데이터 검사 후 인증정보 저장 및 메일 전송" +
            "\n저장한 정보는 관리자만 볼 수 있어서 URI 리턴 없음.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원정보 입력 성공 및 메일 발송 완료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "사용할 수 없는 비밀번호 입니다. (8~20자 이내 영문, 숫자, 특수문자를 모두 포함)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러입니다. 관리자에게 문의해주세요.")
    })
    @ResponseStatus(OK)
    @PostMapping("/join")
    public ApiResponse join(@RequestBody JoinRequest joinRequest) {
        return new ApiResponse(authService.join(joinRequest));
    }

    @Tag(name = "auth")
    @Operation(summary = "회원가입", description = "재학생 인증 완료 시 회원가입, 동아리원은 추가 관리자 인증 필요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공에 성공했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 가입정보입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "인증번호가 일치하지 않습니다."),
    })
    @ResponseStatus(OK)
    @PostMapping("/sign-up")
    public ApiResponse signup(@RequestBody SignUpRequest signUpRequest) {
        return new ApiResponse(authService.signUp(signUpRequest.getEmail(), signUpRequest.getCertification()));
    }

    @Tag(name = "auth")
    @Operation(summary = "로그인")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 발급"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @ResponseStatus(OK)
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest) {
        return new ApiResponse(authService.login(loginRequest.getLoginId(), loginRequest.getPassword()));
    }

    @Tag(name = "auth")
    @Operation(summary = "JWT 재발급")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "JWT 토큰 재발급 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않는 토큰입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "권한이 없는 토큰입니다."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "토큰의 정보가 일치하지 않습니다.")
    })
    @ResponseStatus(OK)
    @PostMapping("/reissue")
    public ApiResponse reissue(@RequestBody TokenRequest tokenRequest) {
        return new ApiResponse(authService.reissueToken(tokenRequest.getAccessToken(), tokenRequest.getRefreshToken()));
    }
}
