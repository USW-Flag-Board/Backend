package com.Flaground.backend.module.report;

import com.Flaground.backend.common.IntegrationTest;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.member.domain.Avatar;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.enums.Role;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.domain.Reply;
import com.Flaground.backend.module.post.domain.repository.PostRepository;
import com.Flaground.backend.module.post.domain.repository.ReplyRepository;
import com.Flaground.backend.module.report.controller.dto.request.ContentReportRequest;
import com.Flaground.backend.module.report.controller.dto.request.MemberReportRequest;
import com.Flaground.backend.module.report.controller.mapper.ReportMapper;
import com.Flaground.backend.module.report.domain.Report;
import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.Flaground.backend.module.report.domain.ReportData;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import com.Flaground.backend.module.report.domain.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReportControllerTest extends IntegrationTest {
    private static final String BASE_URI = "/reports";

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ReportMapper reportMapper;

    private Member member;

    @BeforeEach
    void testSetup() {
        final String email = "gmlwh124@suwon.ac.kr";
        final String nickname = "john";
        final Role role = Role.ROLE_USER;

        Avatar avatar = Avatar.builder().nickname(nickname).build();

        member = memberRepository.save(Member.builder()
                .email(email)
                .avatar(avatar)
                .role(role)
                .build());

        setSecurityContext(member);
    }

    @Nested
    class 멤버_신고_테스트 {
        private final String loginId = "gmlwh124";
        private final ReportCategory category = ReportCategory.욕설;
        private final String detailReason = "test";
        private final ReportType reportType = ReportType.MEMBER;
        private MemberReportRequest request = MemberReportRequest.builder()
                .target(loginId)
                .reportCategory(category)
                .detailExplanation(detailReason)
                .build();
        private final String uri = BASE_URI + "/member";
        private Member reported;

        @BeforeEach
        private void testSetup() {
            reported = memberRepository.save(Member.builder().loginId(loginId).build());
        }

        @Test
        public void 멤버_신고_성공() throws Exception {
            // given

            // when
            mockMvc.perform(post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andDo(print());

            // then
            ReportData<String> reportData = reportMapper.mapFrom(request);
            assertThat(reportData.getReportType()).isEqualTo(reportType);

            Report report = reportRepository.findAll().get(0);
            assertThat(report.getReporter()).isEqualTo(member.getId());
            assertThat(report.getReported()).isEqualTo(reported.getId());
            assertThat(report.getReportCategory()).isEqualTo(category);
            assertThat(report.getDetailExplanation()).isEqualTo(detailReason);
        }

        @Test
        public void 멤버_신고_실패() throws Exception {
            // given
            reportRepository.save(Report.member(member.getId(), reported.getId(), reportMapper.mapFrom(request)));

            // when
            ResultActions resultActions = mockMvc.perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("errorCode", is(ErrorCode.ALREADY_REPORTED.toString())))
                    .andExpect(jsonPath("message", is(ErrorCode.ALREADY_REPORTED.getMessage())))
                    .andDo(print());
        }
    }

    @Nested
    class 게시글_신고_테스트 {
        private final ReportCategory category = ReportCategory.도배;
        private final String detailReason = "test";
        private final ReportType reportType = ReportType.POST;
        private final String uri = BASE_URI + "/content";
        private Post post;
        private ContentReportRequest request;

        @BeforeEach
        void testSetup() {
            post = postRepository.save(Post.builder().build());
            request = ContentReportRequest.builder()
                    .target(post.getId())
                    .reportType(reportType)
                    .reportCategory(category)
                    .detailExplanation(detailReason)
                    .build();
        }

        @Test
        public void 게시글_신고_성공() throws Exception {
            // given

            // when
            mockMvc.perform(post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andDo(print());

            // then
            ReportData<Long> reportData = reportMapper.mapFrom(request);
            assertThat(reportData.getTarget()).isEqualTo(post.getId());
            assertThat(reportData.getReportType()).isEqualTo(reportType);
            assertThat(reportData.getReportCategory()).isEqualTo(category);
            assertThat(reportData.getDetailExplanation()).isEqualTo(detailReason);

            Report report = reportRepository.findAll().get(0);
            assertThat(report.getReporter()).isEqualTo(member.getId());
            assertThat(report.getReported()).isEqualTo(post.getId());
        }

        @Test
        public void 게시글_신고_실패() throws Exception {
            // given
            reportRepository.save(Report.content(member.getId(), reportMapper.mapFrom(request)));

            // when
            ResultActions resultActions = mockMvc.perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("errorCode", is(ErrorCode.ALREADY_REPORTED.toString())))
                    .andExpect(jsonPath("message", is(ErrorCode.ALREADY_REPORTED.getMessage())))
                    .andDo(print());
        }
    }

    @Nested
    class 댓글_신고_테스트 {
        private final ReportCategory category = ReportCategory.홍보;
        private final String detailReason = "test";
        private final ReportType reportType = ReportType.REPLY;
        private final String uri = BASE_URI + "/content";
        private Reply reply;
        private ContentReportRequest request;

        @BeforeEach
        void testSetup() {
            reply = replyRepository.save(Reply.builder().build());
            request = ContentReportRequest.builder()
                    .target(reply.getId())
                    .reportType(reportType)
                    .reportCategory(category)
                    .detailExplanation(detailReason)
                    .build();
        }

        @Test
        public void 댓글_신고_성공() throws Exception {
            // given

            // when
            mockMvc.perform(post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andDo(print());

            // then
            ReportData<Long> reportData = reportMapper.mapFrom(request);
            assertThat(reportData.getTarget()).isEqualTo(reply.getId());
            assertThat(reportData.getReportType()).isEqualTo(reportType);
            assertThat(reportData.getReportCategory()).isEqualTo(category);
            assertThat(reportData.getDetailExplanation()).isEqualTo(detailReason);

            Report report = reportRepository.findAll().get(0);
            assertThat(report.getReporter()).isEqualTo(member.getId());
            assertThat(report.getReported()).isEqualTo(reply.getId());
        }

        @Test
        public void 댓글_신고_실패() throws Exception {
            // given
            reportRepository.save(Report.content(member.getId(), reportMapper.mapFrom(request)));

            // when
            ResultActions resultActions = mockMvc.perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("errorCode", is(ErrorCode.ALREADY_REPORTED.toString())))
                    .andExpect(jsonPath("message", is(ErrorCode.ALREADY_REPORTED.getMessage())))
                    .andDo(print());
        }
    }

    private void setSecurityContext(Member member) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> String.valueOf(member.getRole()));

        UserDetails principal = new User(String.valueOf(member.getId()), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}
