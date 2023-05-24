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
import com.Flaground.backend.module.report.domain.*;
import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import com.Flaground.backend.module.report.domain.repository.ReportRepository;
import org.junit.jupiter.api.AfterEach;
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

    private Member reporter;
    private Member reported;

    @BeforeEach
    void testSetup() {
        final String loginId = "gmlwh124";
        final String email = "gmlwh124@suwon.ac.kr";
        final String nickname = "john";
        final Role role = Role.ROLE_USER;

        Avatar avatar = Avatar.builder().nickname(nickname).build();

        reporter = memberRepository.save(Member.builder()
                .email(email)
                .avatar(avatar)
                .role(role)
                .build());

        reported = memberRepository.save(Member.builder().loginId(loginId).build());

        setSecurityContext(reporter);
    }

    @AfterEach
    void cleanUp() {
        reportRepository.deleteAll();
    }

    @Nested
    class 멤버_신고_테스트 {
        private final String loginId = "gmlwh124";
        private final ReportCategory category = ReportCategory.욕설;
        private final String detailReason = "test";
        private final ReportType reportType = ReportType.MEMBER;
        private MemberReportRequest request = MemberReportRequest.builder()
                .loginId(loginId)
                .reportCategory(category)
                .detailExplanation(detailReason)
                .build();
        private final String uri = BASE_URI + "/member";

        @Test
        void 멤버_신고_성공() throws Exception {
            // given

            // when
            mockMvc.perform(post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andDo(print());

            // then
            MemberReport report = (MemberReport) reportRepository.findAll().get(0);
            assertThat(report.getLoginId()).isEqualTo(loginId);
            assertThat(report.getReportInfo().getReportType()).isEqualTo(reportType);
            assertThat(report.getReportInfo().getReportCategory()).isEqualTo(category);
            assertThat(report.getReportInfo().getDetailExplanation()).isEqualTo(detailReason);
        }

        @Test
        void 멤버_신고_실패() throws Exception {
            // given
            Report report = MemberReport.of(reporter.getId(), reported.getId(), reportMapper.mapFrom(request));
            reportRepository.save(report);

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
        private final String uri = BASE_URI + "/post";
        private Post post;
        private ContentReportRequest request;

        @BeforeEach
        void testSetup() {
            post = postRepository.save(Post.builder().member(reported).build());
            request = ContentReportRequest.builder()
                    .target(post.getId())
                    .reportType(reportType)
                    .reportCategory(category)
                    .detailExplanation(detailReason)
                    .build();
        }

        @Test
        void 게시글_신고_성공() throws Exception {
            // given

            // when
            mockMvc.perform(post(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andDo(print());

            // then
            PostReport report = (PostReport) reportRepository.findAll().get(0);
            assertThat(report.getPostId()).isEqualTo(post.getId());
            assertThat(report.getReportInfo().getReportType()).isEqualTo(reportType);
            assertThat(report.getReportInfo().getReportCategory()).isEqualTo(category);
            assertThat(report.getReportInfo().getDetailExplanation()).isEqualTo(detailReason);
        }

        @Test
        void 게시글_신고_실패() throws Exception {
            // given
            final String board = "자유게시판";
            Report report = PostReport.of(reporter.getId(), reported.getId(), board, reportMapper.mapFrom(request));
            reportRepository.save(report);

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
        private final String uri = BASE_URI + "/reply";
        private Post post;
        private Reply reply;
        private ContentReportRequest request;

        @BeforeEach
        void testSetup() {
            post = postRepository.save(Post.builder().member(reporter).build());
            reply = replyRepository.save(Reply.of(reported, post.getId(), "test"));
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
            ReplyReport report = (ReplyReport) reportRepository.findAll().get(0);
            assertThat(report.getPostId()).isEqualTo(post.getId());
            assertThat(report.getReplyId()).isEqualTo(reply.getId());
            assertThat(report.getReportInfo().getReportType()).isEqualTo(reportType);
            assertThat(report.getReportInfo().getReportCategory()).isEqualTo(category);
            assertThat(report.getReportInfo().getDetailExplanation()).isEqualTo(detailReason);
        }

        @Test
        public void 댓글_신고_실패() throws Exception {
            // given
            Report report = ReplyReport.of(reporter.getId(), reported.getId(), post.getId(), reportMapper.mapFrom(request));
            reportRepository.save(report);

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
