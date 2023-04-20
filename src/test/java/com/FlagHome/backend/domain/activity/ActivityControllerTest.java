package com.FlagHome.backend.domain.activity;


import com.FlagHome.backend.common.IntegrationTest;
import com.FlagHome.backend.domain.activity.controller.dto.request.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.entity.enums.Role;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ActivityControllerTest extends IntegrationTest {
    private static final String BASE_URL = "/activities";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ActivityRepository activityRepository;

    private Member member;

    @BeforeEach
    void setup() {
        final String loginId = "gmlwh124";
        final String name = "문희조";
        final String email = "gmlwh124@suwon.ac.kr";
        final Role role = Role.ROLE_CREW;

        member = memberRepository.save(Member.builder()
                .loginId(loginId)
                .name(name)
                .email(email)
                .role(role)
                .build());

        setSecurityContext(member);
    }

    @Test
    public void 활동_생성_테스트() throws Exception {
        // given
        final String name = "name";
        final String description = "discription";
        final String githubLink = "link";
        final ActivityType activityType = ActivityType.PROJECT;
        final Proceed proceed = Proceed.BOTH;

        ActivityRequest request = ActivityRequest.builder()
                .name(name)
                .description(description)
                .activityType(activityType)
                .githubLink(githubLink)
                .proceed(proceed)
                .build();

        // when
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());
        // then
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList.size()).isEqualTo(1);

        Project project = (Project) activityList.get(0);
        assertThat(project.getName()).isEqualTo(name);
        assertThat(project.getDescription()).isEqualTo(description);
        assertThat(project.getGithubLink()).isEqualTo(githubLink);
        assertThat(project.getProceed()).isEqualTo(proceed);
    }

    @Test
    public void 활동_상세보기_테스트() throws Exception {
        // given
        final String name = "name";
        final String description = "discription";
        final String githubLink = "link";
        final ActivityType activityType = ActivityType.PROJECT;
        final Proceed proceed = Proceed.BOTH;

        ActivityRequest request = ActivityRequest.builder()
                .name(name)
                .description(description)
                .activityType(activityType)
                .githubLink(githubLink)
                .proceed(proceed)
                .build();

        Project project = activityRepository.save(Project.from(request));
        final String uri = BASE_URL + "/" + project.getId();

        // when
        ResultActions resultActions = mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.payload."))
                .andDo(print());
    }

    private void setSecurityContext(Member member) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> String.valueOf(member.getRole()));

        UserDetails principal = new User(String.valueOf(member.getId()), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}