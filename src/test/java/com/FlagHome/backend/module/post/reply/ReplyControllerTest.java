package com.FlagHome.backend.module.post.reply;

import com.FlagHome.backend.common.IntegrationTest;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.member.domain.enums.Role;
import com.FlagHome.backend.module.member.domain.repository.MemberRepository;
import com.FlagHome.backend.module.post.controller.dto.request.CreateReplyRequest;
import com.FlagHome.backend.module.post.controller.dto.request.UpdateReplyRequest;
import com.FlagHome.backend.module.post.domain.Like;
import com.FlagHome.backend.module.post.domain.Post;
import com.FlagHome.backend.module.post.domain.Reply;
import com.FlagHome.backend.module.post.domain.repository.LikeRepository;
import com.FlagHome.backend.module.post.domain.repository.PostRepository;
import com.FlagHome.backend.module.post.domain.repository.ReplyRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReplyControllerTest extends IntegrationTest {
    private static final String BASE_URI = "/posts";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private LikeRepository likeRepository;

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
                .member(member)
                .title(title)
                .build());

        setSecurityContext(member);
    }

    @Test
    public void 댓글_생성_테스트() throws Exception {
        // given
        final String content = "content";
        final int replyCountAtBegin = post.getReplies().size();

        CreateReplyRequest request = CreateReplyRequest.from(content);
        final String uri = BASE_URI + "/" + post.getId() + "/reply";

        // when
        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        Post findPost = postRepository.findById(post.getId()).get();
        Reply reply = replyRepository.findAll().get(0);
        assertThat(findPost.getReplies().size()).isEqualTo(replyCountAtBegin + 1);
        assertThat(reply).isNotNull();
        assertThat(reply.getContent()).isEqualTo(content);
        assertThat(reply.isEdited()).isFalse();
    }

    @Nested
    public class 댓글_수정_테스트 {
        @Test
        public void 댓글_수정_성공_테스트() throws Exception {
            // given
            final String content = "content";
            final String newContent = "newContent";
            Reply reply = replyRepository.save(Reply.of(member, post.getId(), content));
            post.addReply(reply);

            UpdateReplyRequest request = UpdateReplyRequest.from(newContent);
            final String uri = BASE_URI + "/replies/" + reply.getId();

            // when
            mockMvc.perform(put(uri)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andDo(print());

            // then
            Reply findReply = replyRepository.findById(reply.getId()).orElse(null);
            assertThat(findReply).isNotNull();
            assertThat(findReply.getContent()).isEqualTo(newContent);
            assertThat(findReply.isEdited()).isTrue();
            assertThat(post.getReplies().get(0).getContent()).isEqualTo(newContent);
            assertThat(post.getReplies().get(0).isEdited()).isTrue();
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

            UpdateReplyRequest request = UpdateReplyRequest.from(newContent);

            Reply reply = replyRepository.save(Reply.of(stranger, post.getId(), content));
            final String url = BASE_URI + "/replies/" + reply.getId();

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
        final String content2 = "content2";

        Reply reply1 = replyRepository.save(Reply.of(member, post.getId(), content));
        Reply reply2 = replyRepository.save(Reply.of(member, post.getId(), content2));

        post.addReply(reply1);
        post.addReply(reply2);

        final int replyCountAtFirst = post.getReplies().size();
        final String uri = BASE_URI + "/" + post.getId() + "/replies/" + reply1.getId();

        // when
        mockMvc.perform(delete(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        List<Reply> replies = replyRepository.findAll();
        assertThat(replies).isNotNull();
        assertThat(replies.size()).isEqualTo(replyCountAtFirst - 1);
        assertThat(replies.get(0).getContent()).isEqualTo(content2);
        assertThat(post.getReplies().size()).isEqualTo(replyCountAtFirst - 1);
    }

    @Nested
    class 댓글_좋아요_테스트 {
        private Reply reply;
        private String uri;

        @BeforeEach
        void initSetup() {
            final String content = "content";
            reply = replyRepository.save(Reply.of(member, post.getId(), content));
            uri = BASE_URI + "/replies/" + reply.getId() + "/like";
        }

        @Test
        public void 댓글_좋아요_성공() throws Exception {
            // given
            final int likeCountAtBegin = reply.getLikeCount();

            // when
            mockMvc.perform(post(uri)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.payload.liked", is(true)))
                    .andExpect(jsonPath("$.payload.likeCount", is(likeCountAtBegin + 1)))
                    .andDo(print());

            // then
            Reply findReply = replyRepository.findById(reply.getId()).orElse(null);
            assertThat(findReply).isNotNull();
            assertThat(findReply.getLikeCount()).isEqualTo(likeCountAtBegin + 1);
            boolean shouldBeTrue = likeRepository.existsByIds(member.getId(), reply.getId());
            assertThat(shouldBeTrue).isTrue();
        }

        @Test
        public void 댓글_좋아요_실패() throws Exception {
            // given
            likeRepository.save(Like.from(member.getId(), reply.getId()));

            // when
            ResultActions resultActions = mockMvc.perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.ALREADY_LIKED.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.ALREADY_LIKED.getMessage()))
                    .andDo(print());
        }

        @Test
        public void 댓글_좋아요_취소_성공() throws Exception {
            // given
            likeRepository.save(Like.from(member.getId(), reply.getId()));
            final int likeCountAtBegin = reply.like();

            // when
            mockMvc.perform(delete(uri)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.liked", is(false)))
                    .andExpect(jsonPath("$.payload.likeCount", is(likeCountAtBegin - 1)))
                    .andDo(print());

            // then
            Reply findReply = replyRepository.findById(reply.getId()).orElse(null);
            assertThat(findReply).isNotNull();
            assertThat(findReply.getLikeCount()).isEqualTo(likeCountAtBegin - 1);
            boolean shouldBeFalse = likeRepository.existsByIds(member.getId(), reply.getId());
            assertThat(shouldBeFalse).isFalse();
        }

        @Test
        public void 댓글_좋아요_취소_실패() throws Exception {
            // given

            // when
            ResultActions resultActions = mockMvc.perform(delete(uri)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.NEVER_LIKED.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.NEVER_LIKED.getMessage()))
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
