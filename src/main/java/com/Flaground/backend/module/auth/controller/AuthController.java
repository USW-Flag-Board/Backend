package com.Flaground.backend.module.auth.controller;

import com.Flaground.backend.global.common.response.ApplicationResponse;
import com.Flaground.backend.module.auth.controller.dto.request.*;
import com.Flaground.backend.global.common.response.DuplicationResponse;
import com.Flaground.backend.module.auth.controller.dto.response.JoinResponse;
import com.Flaground.backend.module.auth.controller.dto.response.SignUpResponse;
import com.Flaground.backend.module.auth.domain.AuthInformation;
import com.Flaground.backend.module.auth.controller.mapper.AuthMapper;
import com.Flaground.backend.module.auth.service.AuthService;
import com.Flaground.backend.module.token.dto.TokenRequest;
import com.Flaground.backend.module.token.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final AuthMapper authMapper;

    @Tag(name = "auth")
    @Operation(summary = "아이디 중복 체크")
    @ApiResponse(responseCode = "200", description = "중복 체크 성공")
    @ResponseStatus(OK)
    @PostMapping("/check/id")
    public ApplicationResponse<DuplicationResponse> checkId(@RequestBody @Valid CheckLoginIdRequest checkLoginIdRequest) {
        boolean isExist = authService.validateDuplicateLoginId(checkLoginIdRequest.getLoginId());
        return new ApplicationResponse<>(DuplicationResponse.from(isExist));
    }

    @Tag(name = "auth")
    @Operation(summary = "이메일 중복 체크", description = "하나의 이메일에 하나의 계정만 생성 가능")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "중복 체크 성공"),
            @ApiResponse(responseCode = "400", description = "이메일 형식이 아닙니다."),
    })
    @ResponseStatus(OK)
    @PostMapping("/check/email")
    public ApplicationResponse<DuplicationResponse> checkEmail(@RequestBody @Valid CheckEmailRequest checkEmailRequest) {
        boolean isExist = authService.validateDuplicateEmail(checkEmailRequest.getEmail());
        return new ApplicationResponse<>(DuplicationResponse.from(isExist));
    }

    @Tag(name = "auth")
    @Operation(summary = "회원 정보 입력 및 메일 전송", description = "작성한 데이터 검사 후 인증정보 저장 및 메일 전송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원정보 입력 성공 및 메일 발송 완료"),
            @ApiResponse(responseCode = "422", description = "사용할 수 없는 비밀번호 입니다. (8~20자 이내 영문, 숫자, 특수문자를 모두 포함)"),
            @ApiResponse(responseCode = "500", description = "서버 에러입니다. 관리자에게 문의해주세요.")
    })
    @ResponseStatus(OK)
    @PostMapping("/join")
    public ApplicationResponse<JoinResponse> join(@RequestBody @Valid JoinRequest joinRequest) {
        AuthInformation authInformation = authMapper.mapFrom(joinRequest);
        String email = authService.join(authInformation);
        return new ApplicationResponse<>(JoinResponse.from(email));
    }

    @Tag(name = "auth")
    @Operation(summary = "회원가입", description = "재학생 인증 완료 시 회원가입, 동아리원은 추가 관리자 인증 필요")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "가입 시간이 만료되었습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 가입정보입니다."),
            @ApiResponse(responseCode = "409", description = "인증번호가 일치하지 않습니다."),
    })
    @ResponseStatus(CREATED)
    @PostMapping("/sign-up")
    public ApplicationResponse<SignUpResponse> signup(@RequestBody @Valid SignUpRequest signUpRequest) {
        AuthInformation authInformation = authService.signUp(signUpRequest.getEmail(), signUpRequest.getCertification());
        return new ApplicationResponse<>(SignUpResponse.from(authInformation));
    }

    @Tag(name = "auth")
    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 발급"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 틀립니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @ResponseStatus(OK)
    @PostMapping("/login")
    public ApplicationResponse<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse response = authService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        return new ApplicationResponse<>(response);
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
    public ApplicationResponse<TokenResponse> reissue(@RequestBody @Valid TokenRequest tokenRequest) {
        TokenResponse response = authService.reissueToken(tokenRequest.getAccessToken(), tokenRequest.getRefreshToken());
        return new ApplicationResponse<>(response);
    }
}
