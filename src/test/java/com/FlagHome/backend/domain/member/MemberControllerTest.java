package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.domain.common.Status;
import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import com.FlagHome.backend.domain.member.avatar.repository.AvatarRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
public class MemberControllerTest {
    private final static String BASE_URL = "/api/members";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member member;

    private Avatar avatar;

    @BeforeEach
    public void testSetting() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();

        member = memberRepository.save(Member.builder()
                .loginId("gmlwh124")
                .email("gmlwh124@suwon.ac.kr")
                .name("문희조")
                .password(passwordEncoder.encode("qwer1234!"))
                .status(Status.GENERAL)
                .role(Role.ROLE_USER)
                .major(Major.컴퓨터SW)
                .phoneNumber("01040380540")
                .studentId("19017041")
                .build());

        avatar = avatarRepository.save(Avatar.builder()
                .nickName("john")
                .member(member)
                .bio("안녕하세요?")
                .profileImg("default")
                .build());

        setJwtInformation(member.getId());
    }

    @Test
    @DisplayName("내 정보 가져오기 테스트")
    void getMyProfileTest() throws Exception {
        // given

        // when
//        mockMvc.perform(get(BASE_URL))
//                .

        // then
    }


    private void setJwtInformation(long memberId) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> "ROLE_USER");

        UserDetails principal = new User(Long.toString(memberId), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}
