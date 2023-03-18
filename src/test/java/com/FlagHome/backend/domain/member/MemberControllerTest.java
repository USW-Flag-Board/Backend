package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.global.common.Status;
import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import com.FlagHome.backend.domain.member.avatar.repository.AvatarRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
public class MemberControllerTest {
    private final static String BASE_URL = "/members";

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

    @Autowired
    private MemberService memberService;

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

    @Test
    @DisplayName("회원 이름으로 검색 테스트")
    void searchMemberByName() throws Exception {

        //given
        String name = "길동";

        Member member1 = Member.builder()
                .major(Major.정보보호)
                .id(1L)
                .name("홍길동")
                .build();

        memberRepository.save(member1);
        memberRepository.flush();

        //when, then
        mockMvc.perform(get(BASE_URL+"/search")
                .accept(MediaType.APPLICATION_JSON)
                        .param("name", String.valueOf(name))
        ).andExpect(status().isOk())
         .andExpect(jsonPath("$.payload[0].name", containsString(name)))
         .andExpect(jsonPath("$.payload[0].major", containsString("정보보호")));
    }


    private void setJwtInformation(long memberId) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> "ROLE_USER");

        UserDetails principal = new User(Long.toString(memberId), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}
