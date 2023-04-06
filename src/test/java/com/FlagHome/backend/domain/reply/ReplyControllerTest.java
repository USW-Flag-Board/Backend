package com.FlagHome.backend.domain.reply;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.entity.enums.Role;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.reply.controller.dto.CreateReplyRequest;
import com.FlagHome.backend.domain.reply.controller.dto.UpdateReplyRequest;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.FlagHome.backend.domain.reply.repository.ReplyRepository;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ReplyControllerTest {
    private static final String BASE_URL = "/replies";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    private Member member;

    private Post post;

    @BeforeEach
    public void testSetup() {
        final String email = "gmlwh124@suwon.ac.kr";
        final Role role = Role.ROLE_USER;
        final String title = "title";

        member = memberRepository.save(Member.builder()
                .email(email)
                .role(role)
                .build());

        post = postRepository.save(Post.builder()
                .title(title)
                .build());

        setSecurityContext(member);
    }

    @Test
    public void 댓글_생성_테스트() throws Exception {
        // given
        final String content = "content";

        CreateReplyRequest request = CreateReplyRequest.builder()
                .postId(post.getId())
                .content(content)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andExpect(status().isCreated());
    }
    @Nested
    public class 댓글_수정_테스트 {
        @Test
        public void 댓글_수정_성공_테스트() throws Exception {
            // given
            final String content = "content";
            final String newContent = "newContent";

            Reply reply = replyRepository.save(Reply.of(member, post, content));

            UpdateReplyRequest request = new UpdateReplyRequest(newContent);
            String url = BASE_URL + "/" + reply.getId();

            // when
            mockMvc.perform(put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andDo(print());

            // then
            Reply findReply = replyRepository.findById(reply.getId()).orElse(null);
            assertThat(findReply).isNotNull();
            assertThat(findReply.getContent()).isEqualTo(newContent);
        }

        @Test
        public void 댓글_수정_실패_테스트() throws Exception {
            // given
            final String email = "hejow124@suwon.ac.kr";
            final String content = "content";
            final String newContent = "newContent";

            Member stranger = memberRepository.save(Member.builder()
                    .email(email)
                    .role(Role.ROLE_USER)
                    .build());

            UpdateReplyRequest request = new UpdateReplyRequest(newContent);

            Reply reply = replyRepository.save(Reply.of(stranger, post, content));
            String url = BASE_URL + "/" + reply.getId();

            // when
            ResultActions resultActions = mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.NOT_AUTHOR.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.NOT_AUTHOR.getMessage()))
                    .andDo(print());
        }
    }

    @Test
    public void 댓글_삭제_테스트() throws Exception {
        // given
        final String content = "content";

        Reply reply = replyRepository.save(Reply.of(member, post, content));
        String url = BASE_URL + "/" + reply.getId();

        // when
        mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        Reply findReply = replyRepository.findById(reply.getId()).orElse(null);
        assertThat(findReply).isNull();
    }


    private void setSecurityContext(Member member) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> String.valueOf(member.getRole()));

        UserDetails principal = new User(String.valueOf(member.getId()), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}
