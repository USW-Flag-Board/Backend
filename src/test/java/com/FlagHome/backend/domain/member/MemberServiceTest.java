package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.domain.auth.AuthInformation;
import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.UpdateAvatarRequest;
import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import com.FlagHome.backend.domain.member.avatar.repository.AvatarRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.entity.enums.MemberStatus;
import com.FlagHome.backend.domain.member.mapper.MemberMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private AvatarRepository avatarRepository;

    @Test
    @DisplayName("유저 생성 테스트")
    void createMemberTest() {
        // given
        final String loginId = "gmlwh124";
        final String nickName = "John";
        final String email = "gmlwh124@suwon.ac.kr";
        final String password = "qwer1234!";

        AuthInformation authInformation = AuthInformation.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .nickName(nickName)
                .build();

        // when
        memberService.initMember(authInformation);

        // then
        Member member = memberService.findByLoginId(loginId);
        Avatar avatar = avatarRepository.findByMemberId(member.getId()).orElse(null);
        assertThat(avatar).isNotNull();
        assertThat(avatar.getMember().getId()).isEqualTo(member.getId());
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
            boolean notActivate = findMember.isNotActivated();
            assertThat(findMember.getStatus()).isEqualTo(withdraw);
            assertThat(notActivate).isTrue();
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
        @DisplayName("비밀번호 변경 중 같은 비밀번호로 실패")
        void updatePasswordFailTeset() {
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
    @DisplayName("아바타 업데이트 테스트")
    void updateAvatarTest() {
        // given
        String loginId = "gmlwh124";
        String nickName = "john";
        String newNickName = "hejow";

        Member member = memberRepository.save(Member.builder().loginId(loginId).build());
        avatarRepository.save(Avatar.builder()
                .member(member)
                .nickName(nickName)
                .build());

        UpdateAvatarRequest updateAvatarRequest = UpdateAvatarRequest.builder()
                .nickName(newNickName)
                .build();

        // when
        memberService.updateAvatar(member.getId(), memberMapper.toAvatar(updateAvatarRequest));

        // then
        AvatarResponse response = avatarRepository.getAvatar(loginId);
        assertThat(response.getLoginId()).isEqualTo(loginId);
        assertThat(response.getNickName()).isEqualTo(newNickName);
    }

//    @Test
//    @DisplayName("휴면계정으로 분류 테스트")
//    void changeAllToSleepMemberTest() {
//        //given
//        String loginId = "hwyoung123";
//        LocalDateTime lastLoginTime = LocalDateTime.now().minusDays(7);
//        Status status = Status.GENERAL;
//
//        Member member = memberRepository.save(Member.builder()
//                .loginId(loginId)
//                .build());
//
//        //when
//        memberService.changeAllToSleepMember();
//
//        //then
//        assertThat(member.getStatus() == Status.SLEEPING).isTrue();
//    }

//    @Test
//    @DisplayName("로그보기")
//    void viewLogTest() {
//        //given
//        String loginId = "minjung123";
//        String name = "김민정";
//        LocalDateTime lastLoginTime = LocalDateTime.of(2023,2,5,2,59);
//
//        Member member = memberRepository.save(Member.builder()
//                .loginId(loginId)
//                .name(name)
//                .lastLoginTime(lastLoginTime)
//                .build());
//
//        //when
//        List<LoginLogResponse> memberList = memberService.getAllLoginLogs();
//
//        //then
//        LoginLogResponse testMember = memberList.get(0);
//
//        assertThat(testMember.getId()).isEqualTo(member.getId());
//        assertThat(testMember.getName()).isEqualTo(member.getName());
//        assertThat(testMember.getLastLoginTime()).isEqualTo(member.getLastLoginTime());
//    }

//    @Nested
//    @DisplayName("프로필 가져오기 테스트")
//    class getProfileTest {
//        @Test
//        @DisplayName("유저 정보가 없어서 실패")
//        void getProfileFailTest() {
//            // given
//            String wrongLoginId = "hejow124";
//
//            assertThatExceptionOfType(CustomException.class)
//                    .isThrownBy(() -> memberService.getMemberProfile(wrongLoginId))
//                    .withMessage(ErrorCode.USER_NOT_FOUND.getMessage());
//        }
//
//        @Test
//        @DisplayName("프로필 가져오기 성공")
//        void getProfileSuccessTest() {
//            // given
//            String loginId = "gmlwh124";
//            String bio = "안녕하세요";
//            String profileImg = "url";
//
//            Member member = memberRepository.saveAndFlush(Member.builder()
//                            .loginId(loginId)
//                            .bio(bio)
//                            .profileImg(profileImg)
//                            .build());
//
//            // when
//            MyPageResponse profileResponse = memberService.getMyPage(loginId);
//
//            // then
//            assertThat(profileResponse.getBio()).isEqualTo(bio);
//            assertThat(profileResponse.getProfileImg()).isEqualTo(profileImg);
//        }
//    }
}
