package com.FlagHome.backend.v1.member;

import com.FlagHome.backend.v1.member.dto.UpdateProfileRequest;
import com.FlagHome.backend.v1.member.entity.Member;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.v1.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

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

        // then
        Member member = memberRepository.findById(memberEntity.getId()).get();
        assertThat(member.getId()).isEqualTo(memberEntity.getId());
        assertThat(member.getBio()).isNotEqualTo(memberEntity.getBio());
        assertThat(member.getPhoneNumber()).isNotEqualTo(memberEntity.getPhoneNumber());

    }
}
