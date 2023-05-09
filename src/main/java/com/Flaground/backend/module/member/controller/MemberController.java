package com.Flaground.backend.module.member.controller;

import com.Flaground.backend.global.common.ApplicationResponse;
import com.Flaground.backend.global.utility.SecurityUtils;
import com.Flaground.backend.module.member.controller.dto.request.*;
import com.Flaground.backend.module.member.controller.dto.response.*;
import com.Flaground.backend.module.member.controller.mapper.MemberMapper;
import com.Flaground.backend.module.member.domain.Avatar;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.service.MemberService;
import com.Flaground.backend.module.token.domain.Token;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "member", description = "멤버 API")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Tag(name = "member")
    @Operation(summary = "멤버 페이지 정보 가져오기 (아바타)", description = "아바타 정보 가져오기.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 아바타 불러오기 성공"),
            @ApiResponse(responseCode = "400", description = "탈퇴한 유저, 리다이렉트 해줄 것"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @ResponseStatus(OK)
    @GetMapping("/{loginId}")
    public ApplicationResponse<AvatarResponse> getMemberPageAvatar(@PathVariable String loginId) {
        AvatarResponse response = memberService.getMemberPageAvatar(loginId);
        return new ApplicationResponse<>(response);
    }

    @Tag(name = "member")
    @Operation(summary = "내 상세정보 보기", description = "[토큰 필요] 내 상세정보 보기.\n" +
            "프로필 정보와 개인 정보를 가져온다. (프로필 정보란, 개인 정보가 아닌 가변적인 정보)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 정보를 가져왔습니다."),
            @ApiResponse(responseCode = "401", description = "토큰을 넣지 않으면 401 발생")
    })
    @ResponseStatus(OK)
    @GetMapping
    public ApplicationResponse<MyProfileResponse> getMyProfile() {
        MyProfileResponse response = memberService.getMyProfile(SecurityUtils.getMemberId());
        return new ApplicationResponse(response);
    }

    @Tag(name = "member")
    @Operation(summary = "이름으로 회원 검색", description = "이름, 전공 정보를 리스트로 반환합니다. 없으면 null을 리턴합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버 검색 리스트 가져오기에 성공하였습니다."),
    })
    @GetMapping("/search")
    public ApplicationResponse searchMemberByName(@RequestParam(value = "name") String name) {
        List<SearchMemberResponse> response = memberService.searchMember(name);
        return new ApplicationResponse(response);
    }

    @Tag(name = "member")
    @Operation(summary = "아이디 찾기", description = "존재하는 멤버라면 인증 이메일 발송한다.\n" +
                                                    "201이 뜬다면 인증을 진행한다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "조회에 성공해 메일을 발송합니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "409", description = "이메일과 이름이 일치하지 않습니다."),
    })
    @ResponseStatus(CREATED)
    @PostMapping("/find/id")
    public ApplicationResponse<RecoveryResponse> findId(@RequestBody @Valid FindIdRequest findIdRequest) {
        Token token = memberService.findId(findIdRequest.getName(), findIdRequest.getEmail());
        return new ApplicationResponse<>(memberMapper.toRecoveryResponse(token));
    }

    @Tag(name = "member")
    @Operation(summary = "비밀번호 찾기", description = "존재하는 멤버라면 인증 이메일 발송한다.\n" +
                                                            "201이 뜬다면 인증을 진행한다")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "조회에 성공해 메일을 발송합니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "409", description = "이메일과 아이디가 일치하지 않습니다."),
    })
    @ResponseStatus(CREATED)
    @PostMapping("/find/password")
    public ApplicationResponse<RecoveryResponse> findPassword(@RequestBody @Valid FindPasswordRequest findPasswordRequest) {
        Token token = memberService.findPassword(findPasswordRequest.getLoginId(), findPasswordRequest.getEmail());
        return new ApplicationResponse<>(memberMapper.toRecoveryResponse(token));
    }

    @Tag(name = "member")
    @Operation(summary = "인증번호 인증하기", description = "아이디/비밀번호 찾기 이후 인증 단계, 유저 아이디와 이메일을 리턴한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "유효하지 않는 토큰입니다."),
            @ApiResponse(responseCode = "404", description = "아이디/비밀번호 찾기 요청이 존재하지 않습니다."),
            @ApiResponse(responseCode = "409", description = "인증번호가 일치하지 않습니다.")
    })
    @ResponseStatus(OK)
    @PostMapping("/certification")
    public ApplicationResponse<RecoveryResultResponse> verifyCertification(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        Member member = memberService.verifyCertification(authenticationRequest.getEmail(), authenticationRequest.getCertification());
        return new ApplicationResponse<>(memberMapper.toRecoveryResult(member));
    }

    @Tag(name = "member")
    @Operation(summary = "프로필 사진 업데이트하기", description = "[토큰 필요] 개인 프로필 이미지 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 사진 변경완료"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류입니다. 관리자에게 문의주세요.")
    })
    @ResponseStatus(OK)
    @PostMapping("/avatar/image")
    public ApplicationResponse updateProfileImage(@RequestPart("image") MultipartFile profileImage) {
        memberService.updateProfileImage(SecurityUtils.getMemberId(), profileImage);
        return new ApplicationResponse<>();
    }

    @Tag(name = "member")
    @Operation(summary = "프로필 기본 이미지로 변경", description = "[토큰 필요]")
    @ApiResponses({})
    @ResponseStatus(OK)
    @PutMapping("/avatar/reset")
    public ApplicationResponse resetProfileImage() {
        memberService.resetProfileImage(SecurityUtils.getMemberId());
        return new ApplicationResponse<>();
    }

    @Tag(name = "member")
    @Operation(summary = "비밀번호 수정", description = "비밀번호 찾기로 새로운 비밀번호를 설정하는 경우")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "새로운 비밀번호를 변경했습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
    })
    @ResponseStatus(OK)
    @PutMapping("/find/password")
    public ApplicationResponse changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        memberService.changePassword(changePasswordRequest.getEmail(), changePasswordRequest.getNewPassword());
        return new ApplicationResponse();
    }

    @Tag(name = "member")
    @Operation(summary = "비밀번호 수정", description = "[토큰 필요] 로그인한 유저가 직접 비밀번호를 변경하는 경우.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호를 수정했습니다. 다시 로그인 해주세요"),
            @ApiResponse(responseCode = "401", description = "토큰을 넣지 않으면 401 발생"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "409", description = "기존과 같은 비밀번호는 사용할 수 없습니다."),
    })
    @ResponseStatus(OK)
    @PutMapping("/password")
    public ApplicationResponse updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        memberService.updatePassword(SecurityUtils.getMemberId(), updatePasswordRequest.getCurrentPassword(), updatePasswordRequest.getNewPassword());
        return new ApplicationResponse();
    }

    @Tag(name = "member")
    @Operation(summary = "아바타 수정하기", description = "[토큰 필요] 개인 프로필 중 가벼운 정보만 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 수정을 완료하였습니다."),
            @ApiResponse(responseCode = "401", description = "토큰을 넣지 않으면 401 발생")
    })
    @ResponseStatus(OK)
    @PutMapping("/avatar")
    public ApplicationResponse updateAvatar(@RequestBody @Valid UpdateAvatarRequest updateAvatarRequest) {
        Avatar avatar = memberMapper.mapFrom(updateAvatarRequest);
        memberService.updateAvatar(SecurityUtils.getMemberId(), avatar);
        return new ApplicationResponse<>();
    }

    @Tag(name = "member")
    @Operation(summary = "회원 탈퇴", description = "[토큰 필요]")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 탈퇴에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "토큰을 넣지 않으면 401 발생"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.")
    })
    @ResponseStatus(OK)
    @PutMapping("/withdraw")
    public ApplicationResponse withdraw(@RequestBody @Valid WithdrawRequest withdrawRequest) {
        memberService.withdraw(SecurityUtils.getMemberId(), withdrawRequest.getCurrentPassword());
        return new ApplicationResponse<>();
    }
}