package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.domain.member.dto.UpdateProfileRequest;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.member.dto.UpdatePasswordRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;
    
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
                            .status(Status.GENERAL)
                            .build());

            // when
            memberService.withdraw(savedMember.getId(), password);

            // then : 정상적으로 상태가 변경되었고 동일한 엔티티인지
            Member member = memberRepository.findById(savedMember.getId()).get();
            assertThat(member.getId()).isEqualTo(savedMember.getId());
            assertThat(member.getLoginId()).isEqualTo(loginId);
            assertThat(member.getStatus()).isEqualTo(Status.WITHDRAW);
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
                    .withMessage("비밀번호가 일치하지 않습니다.");
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
                    .withMessage("기존과 같은 비밀번호는 사용할 수 없습니다.");
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
        assertThat(member.getBio()).isNotEqualTo(memberEntity.getBio());
        assertThat(member.getPhoneNumber()).isNotEqualTo(memberEntity.getPhoneNumber());

    }
}