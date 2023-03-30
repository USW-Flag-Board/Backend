package com.FlagHome.backend.domain.activity;


import com.FlagHome.backend.domain.activity.controller.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.BookUsage;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.entity.enums.Role;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.global.utility.UriCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Ignore
public class ActivityControllerTest {
    private static final String BASE_URL = "/activities";

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Member member;

    @BeforeEach
    void setUp() {
        final String loginId = "gmlwh124";
        final String password = "qwer1234!";
        final String email = "gmlwh124@suwon.ac.kr";
        final Role role = Role.ROLE_CREW;

        member = memberRepository.save(Member.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .role(role)
                .build());

        setJwtInformation(member);
    }

    @Test
    @DisplayName("활동 생성 테스트")
    void createActivityTest() throws Exception {
        // given
        ActivityRequest request = ActivityRequest.builder()
                .name("name")
                .description("description")
                .activityType(ActivityType.PROJECT)
                .proceed(Proceed.OFFLINE)
                .bookUsage(BookUsage.NOT_USE)
                .build();

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    private void setJwtInformation(Member member) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> String.valueOf(member.getRole()));

        UserDetails principal = new User(String.valueOf(member.getId()), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}