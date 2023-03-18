package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.MyProfileResponse;
import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import com.FlagHome.backend.domain.member.avatar.repository.AvatarRepository;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.global.config.QueryDslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QueryDslConfig.class)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Nested
    @DisplayName("아바타 테스트")
    class avatarTest {
        private Member member;

        @BeforeEach
        void testSetup() {
            String loginId = "gmlwh124";
            String name = "문희조";
            String email = "gmlwh124@suwon.ac.kr";
            Major major = Major.컴퓨터SW;
            String studentId = "19017041";
            String phoneNumber = "010-1234-5678";

            member = memberRepository.save(Member.builder()
                            .loginId(loginId)
                            .name(name)
                            .email(email)
                            .major(major)
                            .studentId(studentId)
                            .phoneNumber(phoneNumber)
                            .build());
        }

        @Test
        @DisplayName("아바타 가져오기 테스트")
        void findByMemberIdTest() {
            // given
            String bio = "안녕하세요";
            String nickName = "john";

            avatarRepository.save(Avatar.builder()
                    .member(member)
                    .nickName(nickName)
                    .bio(bio)
                    .build());

            // when
            Optional<Avatar> foundAvatar = avatarRepository.findByMemberId(member.getId());

            // then
            assertThat(foundAvatar.get()).isNotNull();
            assertThat(foundAvatar.get().getNickName()).isEqualTo(nickName);
            assertThat(foundAvatar.get().getBio()).isEqualTo(bio);
        }

        @Test
        @DisplayName("아바타 정보 가져오기 테스트")
        void getAvatarTest() {
            // given
            String bio = "안녕하세요";
            String profileImg = "default";
            String nickName = "john";

            avatarRepository.save(Avatar.builder()
                                    .member(member)
                                    .bio(bio)
                                    .nickName(nickName)
                                    .profileImg(profileImg)
                                    .build());

            // when
            AvatarResponse avatarResponse = avatarRepository.getAvatar(member.getLoginId());

            // then
            assertThat(avatarResponse.getLoginId()).isEqualTo(member.getLoginId());
            assertThat(avatarResponse.getBio()).isEqualTo(bio);
            assertThat(avatarResponse.getNickName()).isEqualTo(nickName);
            assertThat(avatarResponse.getProfileImg()).isEqualTo(profileImg);
        }

        @Test
        @DisplayName("아바타 삭제 테스트")
        void deleteAvatarTest() {
            // given
            avatarRepository.save(Avatar.builder().member(member).build());

            // when
            avatarRepository.deleteByMemberId(member.getId());

            // then
            Optional<Avatar> avatar = avatarRepository.findByMemberId(member.getId());
            assertThat(avatar.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("내 정보 가져오기 테스트")
        void getMyProfile() {
            // given
            String bio = "안녕하세요";
            String profileImg = "default";
            String nickName = "john";

            avatarRepository.save(Avatar.builder()
                    .member(member)
                    .bio(bio)
                    .profileImg(profileImg)
                    .nickName(nickName)
                    .build());

            // when
            MyProfileResponse response = avatarRepository.getMyProfile(member.getId());

            // then
            assertThat(response.getNickName()).isEqualTo(nickName);
            assertThat(response.getBio()).isEqualTo(bio);
            assertThat(response.getProfileImg()).isEqualTo(profileImg);
            assertThat(response.getName()).isEqualTo(member.getName());
            assertThat(response.getEmail()).isEqualTo(member.getEmail());
            assertThat(response.getMajor()).isEqualTo(member.getMajor());
            assertThat(response.getStudentId()).isEqualTo(member.getStudentId());
            assertThat(response.getPhoneNumber()).isEqualTo(member.getPhoneNumber());
        }
    }
}
