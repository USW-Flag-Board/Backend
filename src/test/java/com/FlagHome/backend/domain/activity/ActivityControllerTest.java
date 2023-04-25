package com.FlagHome.backend.domain.activity;


import com.FlagHome.backend.common.IntegrationTest;
import com.FlagHome.backend.domain.activity.controller.dto.request.CreateActivityRequest;
import com.FlagHome.backend.domain.activity.controller.dto.request.UpdateActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.BookUsage;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import com.FlagHome.backend.domain.activity.mapper.ActivityMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ActivityControllerTest extends IntegrationTest {
    private static final String BASE_URL = "/activities";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMapper activityMapper;

    private Member member;

    private Activity activity;

    private CreateActivityRequest createActivityRequest;

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
        setCreateActivityRequest();

        // when
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createActivityRequest)))
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        List<Activity> activityList = activityRepository.findAll();
        assertThat(activityList.size()).isEqualTo(1);

        Activity activity = activityList.get(0);
        assertThat(activity.getName()).isEqualTo(createActivityRequest.getName());
        assertThat(activity.getDescription()).isEqualTo(createActivityRequest.getDescription());
        assertThat(activity.getInfo().getGithubURL()).isEqualTo(createActivityRequest.getGithubURL());
        assertThat(activity.getInfo().getProceed()).isEqualTo(createActivityRequest.getProceed());
        assertThat(activity.getInfo().getBookUsage()).isEqualTo(createActivityRequest.getBookUsage());
    }

    @Test
    public void 활동_상세보기_테스트() throws Exception {
        // given
        setActivity(member);
        final String uri = BASE_URL + "/" + activity.getId();

        // when
        ResultActions resultActions = mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void 활동_신청_체크_테스트() throws Exception {
        // given
        final Role role = Role.ROLE_CREW;
        Member applyMember = memberRepository.save(Member.builder().role(role).build());
        setSecurityContext(applyMember);
        setActivity(member);

        final String uri = BASE_URL + "/" + activity.getId() + "/check";

        // when
        ResultActions resultActions = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("payload").value(Boolean.FALSE))
                .andDo(print());
    }

    @Test
    public void 활동_수정_테스트() throws Exception {
        // given
        setActivity(member);

        final String name = "changed";
        final String description = "changed";
        final Proceed proceed = Proceed.ONLINE;
        final String githubURL = "changed";
        final BookUsage bookUsage = BookUsage.NOT_USE;

        UpdateActivityRequest request = UpdateActivityRequest.builder()
                .name(name)
                .description(description)
                .proceed(proceed)
                .githubURL(githubURL)
                .bookUsage(bookUsage)
                .bookName("")
                .build();

        final String uri = BASE_URL + "/" + activity.getId();

        // when
        mockMvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        Activity changedActivity = activityRepository.findById(activity.getId()).orElse(null);
        assertThat(changedActivity).isNotNull();
        assertThat(changedActivity.getName()).isEqualTo(name);
        assertThat(changedActivity.getDescription()).isEqualTo(description);
        assertThat(changedActivity.getInfo().getGithubURL()).isEqualTo(githubURL);
        assertThat(changedActivity.getInfo().getProceed()).isEqualTo(proceed);
    }

    private void setSecurityContext(Member member) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> String.valueOf(member.getRole()));

        UserDetails principal = new User(String.valueOf(member.getId()), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }

    private void setCreateActivityRequest() {
        final String name = "name";
        final String description = "description";
        final Proceed proceed = Proceed.BOTH;
        final ActivityType activityType = ActivityType.PROJECT;
        final String githubURL = "link";
        final BookUsage bookUsage = BookUsage.NOT_USE;

        createActivityRequest = CreateActivityRequest.builder()
                .name(name)
                .description(description)
                .proceed(proceed)
                .type(activityType)
                .githubURL(githubURL)
                .bookUsage(bookUsage)
                .bookName("")
                .build();
    }

    private void setActivity(Member member) {
        setCreateActivityRequest();
        activity = activityRepository.save(Activity.of(member, activityMapper.toActivity(createActivityRequest)));
    }
}