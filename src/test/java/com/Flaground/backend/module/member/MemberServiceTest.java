package com.Flaground.backend.module.member;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.auth.domain.AuthInformation;
import com.Flaground.backend.module.auth.domain.JoinType;
import com.Flaground.backend.module.member.controller.dto.request.UpdateAvatarRequest;
import com.Flaground.backend.module.member.controller.mapper.MemberMapper;
import com.Flaground.backend.module.member.domain.Avatar;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.Sleeping;
import com.Flaground.backend.module.member.domain.enums.Major;
import com.Flaground.backend.module.member.domain.enums.MemberStatus;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.member.domain.repository.SleepingRepository;
import com.Flaground.backend.module.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Transactional
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private SleepingRepository sleepingRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;

    private Avatar avatar;

    @Test
    public void 유저_생성_테스트() {
        // given
        final String loginId = "gmlwh124";
        final String nickname = "John";
        final String email = "gmlwh124@suwon.ac.kr";
        final String password = "qwer1234!";
        final JoinType joinType = JoinType.NORMAL;

        AuthInformation authInformation = AuthInformation.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .nickname(nickname)
                .joinType(joinType)
                .build();

        // when
        memberService.initMember(authInformation.toJoinMember());

        // then
        Member member = memberService.findByLoginId(loginId);
        assertThat(member.getLoginId()).isEqualTo(loginId);
        assertThat(member.getEmail()).isEqualTo(email);
        boolean shouldBeTrue = passwordEncoder.matches(password, member.getPassword());
        assertThat(shouldBeTrue).isTrue();
        assertThat(member.getAvatar().getNickname()).isEqualTo(nickname);

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
            MemberStatus withdraw = MemberStatus.WITHDRAW;

            Member savedMember = memberRepository.saveAndFlush(Member.builder()
                            .loginId(loginId)
                            .password(passwordEncoder.encode(password))
                            .build());

            // when
            memberService.withdraw(savedMember.getId(), password);
            entityManager.flush();

            // then
            Member findMember = memberRepository.findById(savedMember.getId()).get();
            assertThat(findMember.getStatus()).isEqualTo(withdraw);
            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(findMember::isWithdraw)
                    .withMessage(ErrorCode.UNAVAILABLE_ACCOUNT.getMessage());
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
                            .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.withdraw(saveMember.getId(), wrongPassword))
                    .withMessage(ErrorCode.PASSWORD_NOT_MATCH.getMessage());
        }
    }

    @Nested
    @DisplayName("비밀번호 변경 테스트 - 잊어서 바꾸는 경우")
    class changePasswordTest {
        @Test
        @DisplayName("비밀번호 변경 성공")
        void changePasswordSuccessTest() {
            // given
            String email = "gmlwh124@suwon.ac.kr";
            String newPassword = "qwer1234!";

            Member member = memberRepository.save(Member.builder().email(email).build());

            // when
            memberService.changePassword(email, newPassword);

            // then
            boolean check = passwordEncoder.matches(newPassword, member.getPassword());
            assertThat(check).isTrue();
        }

        @Test
        @DisplayName("비밀번호 유효성 검사 실패로 변경 실패")
        void changeFailByValidateFailTest() {
            String email = "gmlwh124@suwon.ac.kr";
            String wrongPassword = "123456";

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.changePassword(email, wrongPassword));
        }
    }

    @Nested
    @DisplayName("비밀번호 변경 테스트 - 유저가 바꾸는 경우")
    class updatePasswordTest {
        @Test
        @DisplayName("비밀번호 변경 성공")
        void updatePasswordSuccessTest() {
            // given
            String loginId = "gmlwh124";
            String password = "qwer1234!";
            String newPassword = "wert2345@";

            Member savedMember = memberRepository.save(Member.builder()
                            .loginId(loginId)
                            .password(passwordEncoder.encode(password))
                            .build());

            // when
            memberService.updatePassword(savedMember.getId(), password, newPassword);

            // then : 정상적으로 변경되었는고 같은 엔티티인지
            Member member = memberRepository.findById(savedMember.getId()).get();
            assertThat(member.getId()).isEqualTo(savedMember.getId());
            assertThat(member.getLoginId()).isEqualTo(loginId);
            boolean matches = passwordEncoder.matches(newPassword, member.getPassword());
            assertThat(matches).isTrue();
        }

        @Test
        void 비밀번호_변경_실패() {
            String loginId = "gmlwh124";
            String password = "qwer1234!";

            Member savedMember = memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> memberService.updatePassword(savedMember.getId(), password, password))
                    .withMessage(ErrorCode.PASSWORD_IS_SAME.getMessage());
        }
    }

    @Test
    public void 아바타_업데이트_테스트() {
        // given
        final String loginId = "gmlwh124";

        final String newNickname = "hejow";
        final String newStudentId = "234";
        final String newBio = "test";

        initAvatar();
        Member member = memberRepository.save(Member.builder().loginId(loginId).avatar(avatar).build());

        UpdateAvatarRequest updateAvatarRequest = UpdateAvatarRequest.builder()
                .nickname(newNickname)
                .studentId(newStudentId)
                .bio(newBio)
                .build();

        // when
        memberService.updateAvatar(member.getId(), memberMapper.mapFrom(updateAvatarRequest));

        // then
        Avatar findAvatar = memberRepository.findById(member.getId()).get().getAvatar();
        assertThat(findAvatar.getNickname()).isEqualTo(newNickname);
        assertThat(findAvatar.getStudentId()).isEqualTo(newStudentId);
        assertThat(findAvatar.getBio()).isEqualTo(newBio);
    }

    @Test
    void 휴면계정_활성화_테스트() {
        // given
        final String loginId = "gmlwh124";
        initAvatar();

        Member member = memberRepository.save(Member.builder().loginId(loginId).avatar(avatar).build());
        sleepingRepository.save(Sleeping.of(member));
        member.deactivate();

        // when
        Member reactivateMember = memberService.reactivateIfSleeping(loginId);

        // then
        Sleeping sleeping = sleepingRepository.findByLoginId(loginId).orElse(null);
        assertThat(sleeping).isNull();
        assertThat(reactivateMember.getStatus()).isEqualTo(MemberStatus.NORMAL);
        assertThat(reactivateMember.getLoginId()).isEqualTo(loginId);
        assertThat(reactivateMember.getAvatar().getStudentId()).isNull();
        assertThat(reactivateMember.getAvatar().getMajor()).isNull();
    }

    private void initAvatar() {
        final String nickname = "john";
        final Major major = Major.컴퓨터SW;
        final String studentId = "123";

        avatar = Avatar.builder().nickname(nickname).studentId(studentId).major(major).build();
    }
}
