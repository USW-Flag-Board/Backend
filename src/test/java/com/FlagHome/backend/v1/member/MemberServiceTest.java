package com.FlagHome.backend.v1.member;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.member.dto.UpdatePasswordRequest;
import com.FlagHome.backend.v1.member.entity.Member;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.v1.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("아이디 유효성 테스트")
    class validateIdTest {
        @Test
        @DisplayName("아이디가 중복이 아님")
        void validateIdSuccessTest() {
            String loginId = "gmlwh124";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .password(password)
                    .build());

            assertThatNoException()
                    .isThrownBy(() -> memberService.validateDuplicateLoginId("hejow124"));
        }

        @Test
        @DisplayName("아이디가 중복되어 실패")
        void validateIdFailTest() {
            String loginId = "gmlwh124";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .password(password)
                    .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.validateDuplicateLoginId(loginId))
                    .withMessage("이미 존재하는 아이디 입니다.");
        }
    }

    @Nested
    @DisplayName("이메일 유효성 테스트")
    class validateEmailTest {
        @Test
        @DisplayName("수원대 이메일이며 중복이 아님")
        void validateEmailSuccessTest() {
            String loginId = "gmlwh124";
            String email = "gmlwh124@suwon.ac.kr";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .password(password)
                    .build());

            assertThatNoException()
                    .isThrownBy(() -> memberService.validateEmail("hejow124@suwon.ac.kr"));
        }

        @Test
        @DisplayName("수원대 이메일이 아니라 실패")
        void validateUSWEmailFailTest() {
            String loginId = "gmlwh124";
            String email = "gmlwh124@naver.com";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .password(password)
                    .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.validateEmail(email))
                    .withMessage("수원대학교 웹 메일 주소가 아닙니다.");
        }

        @Test
        @DisplayName("수원대 이메일이지만 중복이라서 실패")
        void validateEmailFailTest() {
            String loginId = "gmlwh124";
            String email = "gmlwh124@suwon.ac.kr";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .password(password)
                    .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.validateEmail(email))
                    .withMessage("이미 가입된 이메일 입니다.");
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
        @DisplayName("비밀번호 변경 중 비밀번호 검증 실패로 실패")
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
}