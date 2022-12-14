package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.member.dto.ProfileResponse;
import com.FlagHome.backend.domain.member.dto.UpdatePasswordRequest;
import com.FlagHome.backend.domain.member.dto.UpdateProfileRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Transactional
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("아이디 찾기 테스트")
    class findLoginIdTest {
        @Test
        @DisplayName("수원대 이메일이 아니라서 실패")
        void validateUSWEmailFailTest() {
            String wrongEmail = "gmlwh124@naver.com";

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.checkMemberByEmail(wrongEmail))
                    .withMessage(ErrorCode.NOT_USW_EMAIL.getMessage());
        }

        @Test
        @DisplayName("유저 정보가 존재하지 않아 실패")
        void findLoginIdFailTest() {
            String neverUsedEmail = "hejow124@suwon.ac.kr";

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.checkMemberByEmail(neverUsedEmail))
                    .withMessage(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("유저 정보 조회 성공")
        void findLoginIdSuccessTest() {
            String loginId = "gmlwh124";
            String email = "gmlwh124@suwon.ac.kr";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .build());

            assertThatNoException()
                    .isThrownBy(() -> memberService.checkMemberByEmail(email));
        }
    }

    @Nested
    @DisplayName("비밀번호 재발급 인증 테스트")
    class reissuePasswordTest {
        @Test
        @DisplayName("수원대 이메일이 아니라서 실패")
        void validateUSWEmailFailTest() {
            String loginId = "gmlwh124";
            String wrongEmail = "gmlwh124@naver.com";

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.resetPassword(loginId, wrongEmail))
                    .withMessage(ErrorCode.NOT_USW_EMAIL.getMessage());
        }

        @Test
        @DisplayName("유저 정보가 존재하지 않아 실패")
        void findLoginIdFailTest() {
            String loginId = "gmlwh124";
            String neverUsedEmail = "hejow124@suwon.ac.kr";

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.resetPassword(loginId, neverUsedEmail))
                    .withMessage(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("가입 정보가 일치하지 않아 실패")
        void loginIdAndEmailNotMatchTest() {
            String loginId = "gmlwh124";
            String wrongLoginId = "hejow124";
            String email = "gmlwh124@suwon.ac.kr";

            memberRepository.save(Member.builder()
                            .loginId(loginId)
                            .email(email)
                            .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.resetPassword(wrongLoginId, email))
                    .withMessage(ErrorCode.EMAIL_USER_NOT_MATCH.getMessage());
        }
        
        @Test
        @DisplayName("비밀번호 재발급 인증 성공")
        void reissuePasswordSuccessTest() {
            String loginId = "gmlwh124";
            String email = "gmlwh124@suwon.ac.kr";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .build());

            assertThatNoException()
                    .isThrownBy(() -> memberService.resetPassword(loginId, email));
        }
    }
    
    @Nested
    @DisplayName("유저 탈퇴 테스트")
    class withdrawTest {
        @Test
        @DisplayName("유저 탈퇴 성공")
        void withDrawSuccessTest() {
            // given
            String loginId = "gmlwh124";
            String password = "1234";

            Member savedMember = memberRepository.saveAndFlush(Member.builder()
                            .loginId(loginId)
                            .password(passwordEncoder.encode(password))
                            .build());

            // when
            memberService.withdraw(savedMember.getId(), password);
            entityManager.flush();

            // then : 정상적으로 탈퇴되어 멤버 정보가 레포에 없는지
            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberRepository.findById(savedMember.getId())
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
        }

        @Test
        @DisplayName("비밀번호 불일치로 유저 탈퇴 실패")
        void withdrawFailTest() {
            String loginId = "gmlwh124";
            String password = "1234";
            String wrongPassword = "2345";

            Member saveMember =  memberRepository.saveAndFlush(Member.builder()
                            .loginId(loginId)
                            .password(passwordEncoder.encode(password))
                            .status(Status.GENERAL)
                            .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.withdraw(saveMember.getId(), wrongPassword))
                    .withMessage(ErrorCode.PASSWORD_NOT_MATCH.getMessage());
        }
    }

    @Nested
    @DisplayName("비밀번호 변경 테스트")
    class updatePasswordTest {
        @Test
        @DisplayName("비밀번호 변경 성공")
        void updatePasswordSuccessTest() {
            // given
            String loginId = "gmlwh124";
            String password = "1234";
            String newPassword = "2345";

            Member savedMember = memberRepository.save(Member.builder()
                            .loginId(loginId)
                            .password(passwordEncoder.encode(password))
                            .build());

            UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
                            .currentPassword(password)
                            .newPassword(newPassword)
                            .build();

            // when
            memberService.updatePassword(savedMember.getId(), updatePasswordRequest);

            // then : 정상적으로 변경되었는고 같은 엔티티인지
            Member member = memberRepository.findById(savedMember.getId()).get();
            assertThat(member.getId()).isEqualTo(savedMember.getId());
            assertThat(member.getLoginId()).isEqualTo(loginId);
            boolean matches = passwordEncoder.matches(newPassword, member.getPassword());
            assertThat(matches).isTrue();
        }

        @Test
        @DisplayName("비밀번호 변경 중 같은 비밀번호로 실패")
        void updatePasswordFailTeset() {
            String loginId = "gmlwh124";
            String password = "1234";

            Member savedMember = memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .build());

            UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
                    .currentPassword(password)
                    .newPassword(password)
                    .build();

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.updatePassword(savedMember.getId(), updatePasswordRequest))
                    .withMessage(ErrorCode.PASSWORD_IS_SAME.getMessage());
        }
    }

    @Test
    @DisplayName("멤버 정보 수정")
    void updateTest() {
        // given
        String originalloginId = "gmlwh124";
        String originalpassword = "1234";
        String originalbio = "안녕하세요?";
        String originalphonenumber = "01040380540";

        Member memberEntity = memberRepository.saveAndFlush(Member.builder()
                .loginId(originalloginId)
                .password(originalpassword)
                .bio(originalbio)
                .phoneNumber(originalphonenumber)
                .build());

        String changedbio = "접니다";
        String changedphonenumber = "01049964346";

        UpdateProfileRequest updateProfileRequest = UpdateProfileRequest.builder()
                .bio(changedbio)
                .phoneNumber(changedphonenumber)
                .build();

        // when
        memberService.updateProfile(memberEntity.getId(), updateProfileRequest);

        // then 멤버정보가 제대로 수정되었는지 확인, 수정할 멤버랑 수정된 멤버가 같은 멤버인지 확인
        Member member = memberRepository.findById(memberEntity.getId()).get();
        assertThat(member.getId()).isEqualTo(memberEntity.getId());
        assertThat(member.getBio()).isEqualTo(memberEntity.getBio());
        assertThat(member.getPhoneNumber()).isEqualTo(memberEntity.getPhoneNumber());
    }

    @Nested
    @DisplayName("프로필 가져오기 테스트")
    class getProfileTest {
        @Test
        @DisplayName("유저 정보가 없어서 실패")
        void getProfileFailTest() {
            // given
            String wrongLoginId = "hejow124";

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.getProfile(wrongLoginId))
                    .withMessage(ErrorCode.USER_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("프로필 가져오기 성공")
        void getProfileSuccessTest() {
            // given
            String loginId = "gmlwh124";
            String bio = "안녕하세요";
            String profileImg = "url";

            Member member = memberRepository.saveAndFlush(Member.builder()
                            .loginId(loginId)
                            .bio(bio)
                            .profileImg(profileImg)
                            .build());

            // when
            ProfileResponse profileResponse = memberService.getProfile(loginId);

            // then
            assertThat(profileResponse.getBio()).isEqualTo(bio);
            assertThat(profileResponse.getProfileImg()).isEqualTo(profileImg);
        }
    }
}