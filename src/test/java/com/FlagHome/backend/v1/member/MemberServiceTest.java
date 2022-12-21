package com.FlagHome.backend.v1.member;


import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.v1.member.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;
/*
    @Test
    void initTest() {
        // given
        String loginId = "gmlwh124";
        String password = "1234";
        String email = "gmlwh124@naver.com";
        String bio = "안녕하세요?";
        String phoneNumber = "01040380540";

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId(loginId)
                .password(password)
                .name("문희조")
                .major(Major.컴퓨터SW)
                .studentId("19017041")
                .build();

        Member signUpMember = signUpRequest.toMember(passwordEncoder);
        Member savedMember = memberRepository.saveAndFlush(signUpMember);

        InitRequest initRequest = InitRequest.builder()
                .email(email)
                .bio(bio)
                .phoneNumber(phoneNumber)
                .build();

        // when
        memberService.initMember(savedMember.getId(), initRequest);

        // then : 추가한 정보가 잘 반영이 되었는지
        Member member = memberRepository.findByLoginId(loginId).get();

        assertThat(member.getBio()).isEqualTo(bio);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPhoneNumber()).isEqualTo(phoneNumber);
    }
*/
}
