package com.Flaground.backend.module.auth;

import com.Flaground.backend.common.IntegrationTest;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.auth.controller.dto.request.LoginRequest;
import com.Flaground.backend.module.member.domain.IssueRecord;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.enums.MemberStatus;
import com.Flaground.backend.module.member.domain.enums.Role;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends IntegrationTest {
    private static final String BASE_URI = "/auth";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    private Member member;

    @Nested
    class 로그인_실패_테스트 {
        final String loginId = "gmlwh124";
        final String password = "qwer1234!";
        final Role role = Role.ROLE_USER;
        final String uri = BASE_URI + "/login";
        private LoginRequest request;

        private void initRequest(String password) {
            request = LoginRequest.builder().loginId(loginId).password(password).build();
        }

        private void initMember(IssueRecord issueRecord) {
            member = memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .issueRecord(issueRecord)
                    .role(role)
                    .build());
        }

        @AfterEach
        void clean() {
            memberRepository.deleteAllInBatch();
        }

        @Test
        void 비밀번호_불일치_테스트() throws Exception {
            // given
            IssueRecord issueRecord = new IssueRecord();
            final int expectFailCount = issueRecord.getLoginFailCount() + 1;
            final String wrongPassword = "wert2345@";
            initMember(issueRecord);
            initRequest(wrongPassword);

            // when
            mockMvc.perform(post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.PASSWORD_NOT_MATCH.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.PASSWORD_NOT_MATCH.getMessage()))
                    .andExpect(jsonPath("payload", is(expectFailCount)))
                    .andDo(print());

            // then
            Member findMember = memberRepository.findByLoginId(loginId).orElse(null);
            assertThat(findMember).isNotNull();
            assertThat(findMember.getFailCount()).isEqualTo(expectFailCount);
        }

        @Test
        void 계정_잠금_테스트() throws Exception {
            // given
            IssueRecord issueRecord = new IssueRecord(4, 0, 0);
            initMember(issueRecord);
            initRequest(password);
            memberService.loginFailed(loginId);

            // when
            ResultActions resultActions = mockMvc.perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.LOCKED_ACCOUNT.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.LOCKED_ACCOUNT.getMessage()))
                    .andDo(print());
        }

        @Test
        void 계정_비활성화_테스트() throws Exception {
            // given
            final MemberStatus status = MemberStatus.BANNED;
            member = memberRepository.save(Member.builder().loginId(loginId).status(status).build());
            initRequest(password);

            // when
            ResultActions resultActions = mockMvc.perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.BANNED_ACCOUNT.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.BANNED_ACCOUNT.getMessage()))
                    .andDo(print());
        }
    }
}
